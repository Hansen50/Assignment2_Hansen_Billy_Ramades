package com.example.assignment2_hansenbillyramades.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.assignment2_hansenbillyramades.R
import com.example.assignment2_hansenbillyramades.databinding.ActivityMainBinding
import com.example.assignment2_hansenbillyramades.fragments.HomeFragment
import com.example.assignment2_hansenbillyramades.fragments.SearchFragment
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNav.elevation = 120f
        binding.bottomNav.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_home -> {
                        replaceFragment(HomeFragment())
                        true
                    }

                    R.id.menu_search -> {
                        replaceFragment(SearchFragment())
                        true
                    }

                    else -> false
                }
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}