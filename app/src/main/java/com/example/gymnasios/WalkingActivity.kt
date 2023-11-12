package com.example.gymnasios

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gymnasios.databinding.ActivityWalkingBinding
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class WalkingActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityWalkingBinding
    private lateinit var sensorManager: SensorManager
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    private lateinit var tvStepsTaken: TextView
    private lateinit var progressCircular: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalkingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvStepsTaken = binding.tvStepsTaken
        progressCircular = binding.progressCircular

        loadData()
        resetSteps()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected in this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }

        binding.tvBackFromSteps.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        running = true
    }

    override fun onPause() {
        super.onPause()
        running = false
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event?.values?.get(0) ?: 0f
            val currentSteps = (totalSteps - previousTotalSteps).toInt()
            tvStepsTaken.text = currentSteps.toString()

            progressCircular.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }
        }
    }

    private fun resetSteps() {
        tvStepsTaken.setOnClickListener {
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        tvStepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            tvStepsTaken.text = "0"
            saveData()
            true
        }
    }

    private fun saveData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber: Float = sharedPreferences.getFloat("key1", 0f)
        Log.d("WalkingActivity", "$savedNumber")
        previousTotalSteps = savedNumber
    }
}
