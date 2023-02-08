package com.example.affirmations.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.affirmations.data.daysOfWeek
import com.example.affirmations.schedule.ARG_OBJECT
import com.example.affirmations.schedule.ScheduleFragment

class DaysPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        val fragment = ScheduleFragment()
        fragment.arguments = Bundle().apply {
            putString(ARG_OBJECT, daysOfWeek()[position])
        }
        return fragment
    }

}