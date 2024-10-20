package com.example.assignment2_hansenbillyramades.activity

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2_hansenbillyramades.data.DiaryDatabase
import com.example.assignment2_hansenbillyramades.data.DiaryEntity
import com.example.assignment2_hansenbillyramades.R
import com.example.assignment2_hansenbillyramades.databinding.ActivityDiaryDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class DiaryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryDetailBinding
    private lateinit var diary: DiaryEntity
    private lateinit var db: DiaryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database
        db = DiaryDatabase.getDatabase(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_chevron_left_32)

        diary = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra("diary", DiaryEntity::class.java)!!
        else
            intent.getParcelableExtra("diary")!!

        supportActionBar?.title = diary.title
        binding.tvTitleDiary.text = diary.title
        binding.tvDescriptionDiary.text = diary.description
        binding.tvDate.text = formatDate(diary.date)

    }

    private fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
