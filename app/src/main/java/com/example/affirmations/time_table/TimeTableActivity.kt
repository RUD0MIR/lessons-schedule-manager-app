package com.example.affirmations.time_table

import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.SubjectsAdapter
import com.example.affirmations.adapter.TimeTableAdapter
import com.example.affirmations.data.timeTableList
import com.example.affirmations.databinding.ActivitySubjectsBinding
import com.example.affirmations.databinding.ActivityTimeTableBinding
import com.example.affirmations.model.Subject
import com.example.affirmations.model.TimeTableItem
import com.example.affirmations.subjects.SubjectViewModelFactory
import com.example.affirmations.subjects.SubjectsViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

private const val TAG = "TimeTableActivity"

class TimeTableActivity : FragmentActivity() {

    private val context = this@TimeTableActivity
    private val fragmentManager = FragmentActivity().supportFragmentManager

    private val timeTableViewModel by viewModels<TimeTableViewModel> {
        TimeTableViewModelFactory (context)
    }

    private lateinit var binding: ActivityTimeTableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recycler view and data
        val timeTableAdapter = TimeTableAdapter(
            context = context
        ) { itemView -> adapterOnLongClick(itemView) }

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = timeTableAdapter

        timeTableViewModel.timeTableLiveData.observe(context) {
            it?.let { timeTableList ->
                timeTableAdapter.submitList(
                    timeTableList as MutableList<TimeTableItem>
                )
            }
        }

        //listener for app bar menu
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }


        //mmmmmmmmmm time picker
        binding.addTimetblFab.setOnClickListener {
            showTimePicker()
        }

    }

    private fun showTimePicker() {
        val isSystem24Hour = is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker =
            MaterialTimePicker.Builder()
                .setTheme(R.style.timePickerTheme)
                .setTimeFormat(clockFormat)
                .setTitleText(getString(R.string.add_time_table_item_dialog_title))
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText(getString(R.string.add_option))
                .setNegativeButtonText(getString(R.string.cancel_option))
                .build()

        picker.show(supportFragmentManager, "tag")
    }





    private fun adapterOnLongClick(view: View) {
        showContextMenu(view, R.menu.list_item_menu)
    }

    private fun showContextMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            TODO("menuItem: MenuItem ->")
        }

        popup.show()
    }
}