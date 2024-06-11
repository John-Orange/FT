package com.app.ft

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import java.util.concurrent.TimeUnit

class TrackActivity : AppCompatActivity() {

    private lateinit var tvTrackStatus: TextView
    private lateinit var tvCaloriesBurned: TextView
    private lateinit var imageViewRunning: ImageView
    private lateinit var chronometer: Chronometer
    private var startTime: Long = 0
    private var userWeightKg: Float = 70f
    private var userHeightCm: Float = 178f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        userWeightKg = intent.getFloatExtra("userWeightKg", 70f)
        userHeightCm = intent.getFloatExtra("userHeightCm", 178f)

        tvTrackStatus = findViewById(R.id.tvTrackStatus)
        tvCaloriesBurned = findViewById(R.id.tvCaloriesBurned)
        imageViewRunning = findViewById(R.id.imageRunning)
        chronometer = findViewById(R.id.chronometer)

        imageViewRunning.setImageResource(R.drawable.running)

        val btnStartTracking: Button = findViewById(R.id.btnStartTracking)
        val btnStopTracking: Button = findViewById(R.id.btnStopTracking)

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
        chronometer.base = startTime
        chronometer.start()
        tvTrackStatus.text = "Status: Tracking"
        tvCaloriesBurned.text = "Calories Burned: 0"
    }

    private fun stopTracking() {
        chronometer.stop()
        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime)
        val caloriesBurned = calculateCaloriesBurned(seconds)
        tvTrackStatus.text = "Status: Not Tracking"
        tvCaloriesBurned.text = "Calories Burned: $caloriesBurned"
    }

    private fun calculateCaloriesBurned(seconds: Long): Double {
        val heightMeters = userHeightCm / 100
        val bmi = userWeightKg / (heightMeters * heightMeters)
        val baseCalorieBurnRate = 0.1 / 60
        val bmiAdjustmentFactor = when {
            bmi < 18.5 -> 0.9
            bmi in 18.5..24.9 -> 1.0
            bmi in 25.0..29.9 -> 1.1
            else -> 1.2
        }

        return seconds * baseCalorieBurnRate * bmiAdjustmentFactor * userWeightKg
    }
}
