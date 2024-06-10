package com.app.ft

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import java.util.Calendar

class GoalActivity : AppCompatActivity() {

    private lateinit var etGoal: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var llGoals: LinearLayout

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)

        etGoal = findViewById(R.id.etGoal)
        timePicker = findViewById(R.id.timePicker)
        llGoals = findViewById(R.id.llGoals)
        val btnSaveGoal: Button = findViewById(R.id.btnSaveGoal)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
                requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        btnSaveGoal.setOnClickListener {
            saveGoal()
        }

        loadGoals()
    }

    private fun saveGoal() {
        val goal = etGoal.text.toString()
        val hour = timePicker.hour
        val minute = timePicker.minute
        if (goal.isNotEmpty()) {
            try {
                val sharedPref = getSharedPreferences("FitnessTracker", Context.MODE_PRIVATE)
                val goals = sharedPref.getStringSet("fitness_goals", mutableSetOf())?.toMutableSet()
                goals?.add(goal)
                with(sharedPref.edit()) {
                    putStringSet("fitness_goals", goals)
                    putInt("${goal}_hour", hour)
                    putInt("${goal}_minute", minute)
                    apply()
                }
                addGoalView(goal, hour, minute)
                scheduleNotification(goal, hour, minute)
                etGoal.text.clear()
                Toast.makeText(this, "Goal saved: $goal", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("GoalActivity", "Error saving goal: ${e.message}")
                Toast.makeText(this, "Failed to save goal", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter a goal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadGoals() {
        val sharedPref = getSharedPreferences("FitnessTracker", Context.MODE_PRIVATE)
        val goals = sharedPref.getStringSet("fitness_goals", setOf())
        goals?.forEach { goal ->
            val hour = sharedPref.getInt("${goal}_hour", 0)
            val minute = sharedPref.getInt("${goal}_minute", 0)
            addGoalView(goal, hour, minute)
        }
    }

    private fun addGoalView(goal: String, hour: Int, minute: Int) {
        val goalView = LayoutInflater.from(this).inflate(R.layout.activity_goallist, null)
        val tvGoal = goalView.findViewById<TextView>(R.id.tvGoal)
        val btnCancel = goalView.findViewById<Button>(R.id.btnCancel)

        tvGoal.text = "$goal at ${String.format("%02d:%02d", hour, minute)}"
        btnCancel.setOnClickListener {
            removeGoal(goalView, goal)
        }
        llGoals.addView(goalView)
    }

    private fun removeGoal(goalView: View, goal: String) {
        try {
            llGoals.removeView(goalView)
            val sharedPref = getSharedPreferences("FitnessTracker", Context.MODE_PRIVATE)
            val goals = sharedPref.getStringSet("fitness_goals", mutableSetOf())?.toMutableSet()
            goals?.remove(goal)
            with(sharedPref.edit()) {
                putStringSet("fitness_goals", goals)
                remove("${goal}_hour")
                remove("${goal}_minute")
                apply()
            }
            cancelNotification(goal)
        } catch (e: Exception) {
            Log.e("GoalActivity", "Error removing goal: ${e.message}")
            Toast.makeText(this, "Failed to remove goal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun scheduleNotification(goal: String, hour: Int, minute: Int) {
        try {
            val intent = Intent(this, Notification::class.java).apply {
                putExtra("goal", goal)
            }
            val pendingIntent = PendingIntent.getBroadcast(this, goal.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } catch (e: Exception) {
            Log.e("GoalActivity", "Error scheduling notification: ${e.message}")
        }
    }

    private fun cancelNotification(goal: String) {
        try {
            val intent = Intent(this, Notification::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, goal.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        } catch (e: Exception) {
            Log.e("GoalActivity", "Error canceling notification: ${e.message}")
        }
    }
}
