package com.example.assignment2_hansenbillyramades.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.assignment2_hansenbillyramades.data.PreferenceDataStore
import com.example.assignment2_hansenbillyramades.R
import com.example.assignment2_hansenbillyramades.data.dataStore
import com.example.assignment2_hansenbillyramades.databinding.ActivityOnBoardBinding
import kotlinx.coroutines.launch

class OnBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardBinding
    // Mengambil instance dari PreferenceDataStore secara lazy, yang dimana hanya akan diinisialisasi saat pertama kali diakses.
    private val pref by lazy {
        PreferenceDataStore.getInstance(application.dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = "Welcome to Journey A place for you to express your feelings and also stories about yourself."
        val spannableText = SpannableString(text)

        val startIndex = text.indexOf("Journey")
        val endIndex = startIndex + "Journey".length
        val journeyColor = ContextCompat.getColor(this, R.color.blue)

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

// lifecycle scope adalah corotine serta launch untuk memulai nya agar kode dapat di jalankan
// pref set is welcome dalam coroutine, kita memanggil metode untuk menyimpan bahwa pengguna telah melihat onboard.
// Ini berarti bahwa jika pengguna membuka aplikasi lagi, mereka tidak akan diarahkan ke layar onboarding.