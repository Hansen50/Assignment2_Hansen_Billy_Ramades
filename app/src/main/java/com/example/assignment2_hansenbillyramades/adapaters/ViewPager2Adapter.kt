package com.example.assignment2_hansenbillyramades.adapaters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.assignment2_hansenbillyramades.fragments.HomeFragment
import com.example.assignment2_hansenbillyramades.fragments.SearchFragment

class ViewPager2Adapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SearchFragment()
            else -> throw IllegalStateException("Invalid tab position ${position}")
        }
    }
}