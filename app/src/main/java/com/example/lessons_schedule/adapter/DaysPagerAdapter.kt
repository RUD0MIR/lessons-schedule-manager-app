package com.example.lessons_schedule.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lessons_schedule.data.daysOfWeek
import com.example.lessons_schedule.schedule.ScheduleFragment

class DaysPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        val fragment = ScheduleFragment()
        fragment.arguments = Bundle().apply {
            putString(ScheduleFragment.ARG_DAY_OF_WEEK, daysOfWeek()[position])
        }
        return fragment
    }

}