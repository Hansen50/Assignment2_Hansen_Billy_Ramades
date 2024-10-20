package com.example.assignment2_hansenbillyramades.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.assignment2_hansenbillyramades.PreferenceDataStore
import com.example.assignment2_hansenbillyramades.R
import com.example.assignment2_hansenbillyramades.dataStore
import com.example.assignment2_hansenbillyramades.databinding.ActivityOnBoardBinding
import kotlinx.coroutines.launch

class OnBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardBinding
    private val pref by lazy {
        PreferenceDataStore.getInstance(application.dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = "Welcome to Journey\nA place for you to express your feelings and also stories about yourself."
        val spannableText = SpannableString(text)

        val startIndex = text.indexOf("Journey")
        val endIndex = startIndex + "Journey".length
        val journeyColor = ContextCompat.getColor(this, R.color.green)

        spannableText.setSpan(
            ForegroundColorSpan(journeyColor),
            startIndex,
            endIndex,
            0
        )

        spannableText.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex,
            0
        )

        binding.tvDescriptionTitleOnboard1.text = spannableText

        binding.btnNextOnBoard1.setOnClickListener {
            lifecycleScope.launch {
                pref.setIsWelcome(true)
                val intent = Intent(this@OnBoardActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}