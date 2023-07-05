package com.example.mygamedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(){

    private var score = 0

    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown : Long = 60000
    private var countDownInterval : Long = 1000

    private lateinit var tapMeButton : Button
    private lateinit var gameScoreTextView : TextView
    private lateinit var timeLeftTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        timeLeftTextView = findViewById(R.id.timeLeftTextView)

        //Anonymous Inner class

        val l : OnClickListener = object : OnClickListener{
            override fun onClick(view: View?) {
                incrementScore()
              //  Toast.makeText(this@MainActivity, "Button Clicked", Toast.LENGTH_LONG).show()
            }

        }

        tapMeButton.setOnClickListener(l)

        resetGame()
    }

    private fun incrementScore() {
        if (!gameStarted){
            startGame()
        }

        score = score + 1

        val newScore = getString(R.string.yourScore, score)
        gameScoreTextView.text = newScore
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun resetGame() {
        score = 0

        gameScoreTextView.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object :  CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }

        }
        gameStarted = false

    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, score), Toast.LENGTH_LONG).show()

        resetGame()
    }

}