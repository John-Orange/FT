package com.app.ft

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class BMIActivity : AppCompatActivity() {

    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var tvBmiResult: TextView
    private lateinit var btnTrackActivity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        tvBmiResult = findViewById(R.id.tvBmiResult)
        val btnCalculateBmi: Button = findViewById(R.id.btnCalculateBmi)
        btnTrackActivity = findViewById(R.id.btnTrackActivity)

        btnCalculateBmi.setOnClickListener {
            calculateBmi()
        }

        btnTrackActivity.setOnClickListener {
            val weightStr = etWeight.text.toString()
            if (weightStr.isNotEmpty()) {
                val intent = Intent(this, TrackActivity::class.java)
                intent.putExtra("userWeightKg", weightStr.toFloat())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateBmi() {
        val heightStr = etHeight.text.toString()
        val weightStr = etWeight.text.toString()

        if (heightStr.isNotEmpty() && weightStr.isNotEmpty()) {
            val height = heightStr.toFloat() / 100
            val weight = weightStr.toFloat()
            val bmi = weight / (height * height)
            val bmiCategory = when {
                bmi < 18.5 -> "Underweight"
                bmi in 18.5..24.9 -> "Normal"
                bmi in 25.0..29.9 -> "Overweight"
                else -> "Obese"
            }
            tvBmiResult.text = "BMI Result: %.2f (%s)".format(bmi, bmiCategory)
        } else {
            Toast.makeText(this, "Please enter your height and weight", Toast.LENGTH_SHORT).show()
        }
    }
}
