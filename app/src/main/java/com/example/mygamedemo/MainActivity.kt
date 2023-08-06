package com.example.mygamedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(){

    private var score = 0

    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private var initialCountDown : Long = 60000
    private var countDownInterval : Long = 1000

    private lateinit var tapMeButton : Button
    private lateinit var gameScoreTextView : TextView
    private lateinit var timeLeftTextView : TextView
    private var timeLeftOnTimer: Long = 60000


    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val KEY_SCORE = "KEY_SCORE"
        private const val KEY_TIME_LEFT = "KEY_TIME_LEFT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "On create called")

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

        if (savedInstanceState!=null){
            score = savedInstanceState.getInt(KEY_SCORE)
            timeLeftOnTimer = savedInstanceState.getLong(KEY_TIME_LEFT)
            restoreGame()
        }else{
            resetGame()
        }

    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called.")
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_SCORE, score)
        outState.putLong(KEY_TIME_LEFT, timeLeftOnTimer)
        countDownTimer.cancel()

        Log.d(TAG, "onSaveInstanceState: Saving score: $score & Time Left: $timeLeftOnTimer")
        super.onSaveInstanceState(outState)
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

    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.yourScore, score)
        val restoredTime  = timeLeftOnTimer / 1000

        timeLeftTextView.text = getString(R.string.timeLeft, restoredTime)

        countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval){
                override fun onTick(p0: Long) {
                timeLeftOnTimer = p0

                val timeLeft = p0 / 1000
                timeLeftTextView.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
                endGame()
            }
        }

        startGame()
    }


    private fun resetGame() {
        score = 0

        gameScoreTextView.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountDown / 1000
        timeLeftTextView.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object :  CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.actionAbout){
            showInfo()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showInfo() {
        val dialogMessage =getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setMessage(dialogMessage)
        builder.create().show()

    }

}