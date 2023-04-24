/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lessons_schedule.schedule

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.lessons_schedule.R
import com.example.lessons_schedule.adapter.DaysPagerAdapter
import com.example.lessons_schedule.time_table.TimeTableActivity
import com.example.lessons_schedule.data.daysOfWeek
import com.example.lessons_schedule.databinding.ActivityScheduleBinding
import com.example.lessons_schedule.subjects.SubjectsActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.time.Duration.Companion.days


private const val TAG = "ScheduleActivity"

class ScheduleActivity : FragmentActivity() {

    private lateinit var adapter: DaysPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private val context = this

    private lateinit var binding: ActivityScheduleBinding
    private val model: ScheduleViewModel by viewModels()

    private val calendar = Calendar.getInstance()
    //getting id of current day that correlate with tabLayout's ids
    private val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2
    lateinit var prefs: SharedPreferences
    private var weekStates = emptyArray<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        weekStates = resources.getStringArray(R.array.week_states)
        prefs = getPreferences(MODE_PRIVATE)

        setWeekState(
            prefs.getInt(WEEK_STATE_PREF, UPPER_WEEK)
        )

        //click listener for topAppBar menu
        binding.topAppBar.setNavigationOnClickListener {v: View ->
            showAppBarMenu(v, R.menu.schedule_app_bar_menu)
        }

        //ViewPager
        adapter = DaysPagerAdapter(this)
        viewPager = findViewById(R.id.schedule_pager)
        viewPager.adapter = adapter

        tabLayout = binding.weekTabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = daysOfWeek()[position]
        }.attach()

        selectTabForCurrentDayOfWeek(tabLayout)

        binding.btnWeekState.setOnClickListener {
            when(prefs.getInt(WEEK_STATE_PREF, UPPER_WEEK)) {
                UPPER_WEEK -> {
                    setWeekState(LOWER_WEEK)

                    with (prefs.edit()) {
                        putInt(WEEK_STATE_PREF, LOWER_WEEK)
                        apply()
                    }
                }
                LOWER_WEEK -> {
                    setWeekState(UPPER_WEEK)

                    with (prefs.edit()) {
                        putInt(WEEK_STATE_PREF, UPPER_WEEK)
                        apply()
                    }
                }
            }
        }
    }

    private fun setWeekState(weekState: Int) {
        when(weekState) {
            UPPER_WEEK -> {
                binding.tvWeekState.text = weekStates[UPPER_WEEK]
                binding.btnWeekState.setImageResource(R.drawable.ic_arrow_drop_down)

                model.setWeekState(UPPER_WEEK)
            }
            LOWER_WEEK -> {
                binding.tvWeekState.text = weekStates[LOWER_WEEK]
                binding.btnWeekState.setImageResource(R.drawable.ic_arrow_drop_up)

                model.setWeekState(LOWER_WEEK)
            }
        }
    }

    private fun selectTabForCurrentDayOfWeek(tabLayout: TabLayout) {
        if(currentDayOfWeek != 6) {
            tabLayout.getTabAt(currentDayOfWeek)?.select()

        } else {
            tabLayout.getTabAt(1)?.select()
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.schedule_app_bar_menu, menu)
    }

    private fun showAppBarMenu(view: View, @MenuRes menuRes: Int) {
        val menu = PopupMenu(this@ScheduleActivity, view)
        menu.menuInflater.inflate(menuRes, menu.menu)

        menu.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.subjects_option -> {
                    switchActivities(SubjectsActivity::class.java)
                    true
                }
                R.id.time_table_option -> {
                    switchActivities(TimeTableActivity::class.java)
                    true
                }
                else -> {
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
        menu.show()
    }

    private fun switchActivities(destination: Class<*>) {
        val switchActivityIntent = Intent(this, destination)
        startActivity(switchActivityIntent)
    }

    companion object {
        const val WEEK_STATE_PREF = "weekState"

        const val UPPER_WEEK = 2
        const val NEUTRAL_WEEK = 1//Do not pass this value to the SharedPreferences
        const val LOWER_WEEK = 0
    }
}