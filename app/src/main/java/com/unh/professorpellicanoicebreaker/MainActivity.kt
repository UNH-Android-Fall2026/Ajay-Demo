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
                        onGetQuestionClicked = { getQuestionsFromFirebase() },
                        onSubmitClicked = { setResponseToFirebase() }
                    )
                }
            }
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

    }

    private fun setResponseToFirebase(){
        Log.d("IcebreakerF2026", "Save To DB")
    }

}