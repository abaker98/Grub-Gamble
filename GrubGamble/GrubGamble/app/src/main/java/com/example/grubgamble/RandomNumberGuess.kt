package com.example.grubgamble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import java.util.*

class RandomNumberGuess : AppCompatActivity() {
    var randomNumber = 0
    fun displayResult(result: String?) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    fun clickFunction(view: View?) {
        val numberEditText = findViewById<View>(R.id.editId) as EditText
        val guessNumber = numberEditText.text.toString().toInt()
        if (guessNumber > randomNumber) {
            displayResult("Lower!")
        } else if (guessNumber < randomNumber) {
            displayResult("Higher")
        } else {
            displayResult("That's right. Try again!")
            val rand = Random()
            randomNumber = rand.nextInt(10) + 1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_number_guess)
        val rand = Random()
        randomNumber = rand.nextInt(10) + 1
    }
}