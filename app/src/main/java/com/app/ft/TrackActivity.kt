package com.app.ft

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import java.util.concurrent.TimeUnit

class TrackActivity : AppCompatActivity() {

    private lateinit var tvTrackStatus: TextView
    private lateinit var tvElapsedTime: TextView
    private lateinit var tvCaloriesBurned: TextView
    private var startTime: Long = 0
    private val userWeightKg = 70 // Example weight in kilograms

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        val btnStartTracking: Button = findViewById(R.id.btnStartTracking)
        val btnStopTracking: Button = findViewById(R.id.btnStopTracking)
        tvTrackStatus = findViewById(R.id.tvTrackStatus)
        tvElapsedTime = findViewById(R.id.tvElapsedTime)
        tvCaloriesBurned = findViewById(R.id.tvCaloriesBurned)

        btnStartTracking.setOnClickListener {
            startTracking()
            btnStartTracking.visibility = Button.GONE
            btnStopTracking.visibility = Button.VISIBLE
        }

        btnStopTracking.setOnClickListener {
            stopTracking()
            btnStartTracking.visibility = Button.VISIBLE
            btnStopTracking.visibility = Button.GONE
        }
    }

    private fun startTracking() {
        startTime = SystemClock.elapsedRealtime()
        tvTrackStatus.text = "Status: Tracking"
        tvElapsedTime.text = "Elapsed Time: 0 min"
        tvCaloriesBurned.text = "Calories Burned: 0"
    }

    private fun stopTracking() {
        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime)
        val caloriesBurned = calculateCaloriesBurned(minutes)
        tvTrackStatus.text = "Status: Not Tracking"
        tvElapsedTime.text = "Elapsed Time: $minutes min"
        tvCaloriesBurned.text = "Calories Burned: $caloriesBurned"
    }

    private fun calculateCaloriesBurned(minutes: Long): Double {
        return minutes * 0.1 * userWeightKg
    }
}
