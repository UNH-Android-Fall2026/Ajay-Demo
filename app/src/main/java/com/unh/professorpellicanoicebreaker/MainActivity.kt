package com.unh.professorpellicanoicebreaker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.pipeline.AggregateFunction.Companion.first
import com.unh.professorpellicanoicebreaker.ui.theme.ProfessorPellicanoIcebreakerTheme

class MainActivity : ComponentActivity() {
    private val db = Firebase.firestore
    private var questionBank: MutableList<Questions>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfessorPellicanoIcebreakerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        queryDbOnStart = { getQuestionsFromFirebase() },
                        onGetQuestionClicked = { getQuestion() },
                        onSubmitClicked = { first, last, pref, question, answer ->
                            setResponseToFirebase(first, last, pref, answer, question) }
                    )
                }
            }
        }
    }

    private fun getQuestion(): String {
        return if(questionBank?.isNotEmpty() == true){
            questionBank!!.random().text
        } else {
            "No questions available."
        }
    }

    private fun getQuestionsFromFirebase(){
        Log.d("IcebreakerF2026", "Get From DB")
        db.collection("Questions")
            .get()
            .addOnSuccessListener { result ->
                questionBank = mutableListOf()
                for(document in result) {
                    val question = document.toObject(Questions::class.java)
                    questionBank!!.add(question)
                    Log.d("IcebreakerF2026", "$question")
                }
            }
            .addOnFailureListener { err ->
                Log.w("IcebreakerF2026", "error:", err)
            }
    }

    private fun setResponseToFirebase(
        firstName: String,
        lastName: String,
        prefName: String,
        answer: String,
        question: String,
    ){
        Log.d("IcebreakerF2026", "Save To DB")

        val student = hashMapOf(
            "firstname" to firstName,
            "lastname" to lastName,
            "prefname" to prefName,
            "answer" to answer,
            "question" to question,
            "class" to "Android-F26"
        )

        db.collection("Students")
            .add(student)
            .addOnSuccessListener { docRef ->
                Log.d("IcebreakerF2026", "Saved with ID: ${docRef.id}")

            }
            .addOnFailureListener { err ->
                Log.w("IcebreakerF2026", "error saving:", err)
            }
    }

}