package com.app.ft

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTrackActivity: Button = findViewById(R.id.btnTrackActivity)
        val btnBmiCalculator: Button = findViewById(R.id.btnBmiCalculator)
        val btnGoalActivity: Button = findViewById(R.id.btnGoalActivity)

        btnBmiCalculator.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        btnTrackActivity.setOnClickListener {
            val intent = Intent(this, TrackActivity::class.java)
            startActivity(intent)
        }

        btnGoalActivity.setOnClickListener {
            val intent = Intent(this, GoalActivity::class.java)
            startActivity(intent)
        }
    }
}
