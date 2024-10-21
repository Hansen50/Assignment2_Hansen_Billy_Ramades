package com.example.assignment2_hansenbillyramades.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.assignment2_hansenbillyramades.data.PreferenceDataStore
import com.example.assignment2_hansenbillyramades.data.dataStore
import com.example.assignment2_hansenbillyramades.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val splashDelay = 1500L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(splashDelay)

            val pref = PreferenceDataStore.getInstance(application.dataStore)
            val isWelcome = pref.getIsWelcome()

            val intent = if (isWelcome) {
                Intent(this@SplashScreenActivity, MainActivity::class.java)
            } else {
                Intent(this@SplashScreenActivity, OnBoardActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}