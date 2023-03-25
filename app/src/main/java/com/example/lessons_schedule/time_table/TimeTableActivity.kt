package com.example.lessons_schedule.time_table

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons_schedule.R
import com.example.lessons_schedule.adapter.TimeTableAdapter
import com.example.lessons_schedule.data.model.TimeTableItem
import com.example.lessons_schedule.databinding.ActivityTimeTableBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


private const val TAG = "TimeTableActivity"

class TimeTableActivity : FragmentActivity() {

    private val context = this@TimeTableActivity

    private lateinit var binding: ActivityTimeTableBinding
    private lateinit var timeTableAdapter: TimeTableAdapter
    private lateinit var timeTableViewModel: TimeTableViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recycler view and data
        timeTableAdapter = TimeTableAdapter(
            context = context,
            resources = resources,
            onItemLongClick =  { itemView, timeTableItem, position ->
            adapterOnLongClick(
                itemView,
                timeTableItem,
                position
            ) },
            onIconClick = {timeTableItem -> onIconClick(timeTableItem)})

        recyclerView = binding.recyclerView
        recyclerView.adapter = timeTableAdapter

        timeTableViewModel = ViewModelProvider(context)[TimeTableViewModel::class.java]
        timeTableViewModel.readTimeTableData.observe(context, Observer { timeTableItems ->
            timeTableAdapter.submitList(timeTableItems)
        })

        //listener for app bar menu
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }


        //mmmmmmmmmm time picker
        binding.addTimetblFab.setOnClickListener {
            showAddTimePicker()
        }
    }

    private fun showDeleteTimeTableItemDialog(timeTableItem: TimeTableItem) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.delete_lesson_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                showIconOnLastItem()
                timeTableViewModel.deleteTimeTableItem(timeTableItem)
            }
            .show()
    }

    private fun showIconOnLastItem() {
        if(timeTableAdapter.itemCount > 1) {
            val lastItem = timeTableAdapter.currentList[timeTableAdapter.itemCount - 2]

            val newItem = TimeTableItem(
                lastItem.id,
                lastItem.lessonTime,
                lastItem.lessonNumber,
                isIconDisplayed = true
            )
            timeTableViewModel.updateTimeTableItem(newItem)
        }
    }

    private fun onIconClick(timeTableItem: TimeTableItem) {
        showDeleteTimeTableItemDialog(timeTableItem)
    }

    private fun showAddTimePicker() {
        val isSystem24Hour = DateFormat.is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setTitleText(getString(R.string.add_time_table_item_dialog_title))
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setPositiveButtonText(getString(R.string.add_option))
                .setNegativeButtonText(getString(R.string.cancel_option))
                .build()
        picker.show(supportFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            val hour = getTimeValue(picker.hour.toString())
            val minute = getTimeValue(picker.minute.toString())

            insertDataToDatabase(hour, minute)

            hideIconOnPenultimateItem()
        }
    }

    private fun insertDataToDatabase(hour: String, minute: String) {
        val time = getString(R.string.time_sample, hour, minute)
        val timeTableItem = TimeTableItem(
            0,
            time,
            timeTableAdapter.itemCount + 1,
            isIconDisplayed = true
        )
        timeTableViewModel.addTimeTableItem(timeTableItem)
    }

    private fun hideIconOnPenultimateItem() {
        if(timeTableAdapter.itemCount > 0) {
            val penultimateItem = timeTableAdapter.currentList[timeTableAdapter.itemCount - 1]

            val newItem = TimeTableItem(
                penultimateItem.id,
                penultimateItem.lessonTime,
                penultimateItem.lessonNumber,
                isIconDisplayed = false
            )
            timeTableViewModel.updateTimeTableItem(newItem)
        }
    }

    private fun showEditTimePicker(timeTableItem: TimeTableItem, position: Int) {
        val isSystem24Hour = DateFormat.is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        var hour = timeTableItem.lessonTime.substring(0, 2)
        var minute = timeTableItem.lessonTime.substring(3)

        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setTitleText(getString(R.string.edit_time_table_item_dialog_title))
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setPositiveButtonText(getString(R.string.save_option))
                .setNegativeButtonText(getString(R.string.cancel_option))
                .setHour(hour.toInt())
                .setMinute(minute.toInt())
                .build()
        picker.show(supportFragmentManager, "tag")

        picker.addOnPositiveButtonClickListener {
            hour = getTimeValue(picker.hour.toString())
            minute = getTimeValue(picker.minute.toString())
            updateDataInDatabase(hour, minute, timeTableItem, position)
        }
    }

    private fun updateDataInDatabase(
        hour: String,
        minute: String,
        timeTableItem: TimeTableItem,
        position: Int
    ) {
        val time = getString(R.string.time_sample, hour, minute)
        val isItemLast = position == timeTableAdapter.itemCount - 1

        val newTimeTableItem = TimeTableItem(
            timeTableItem.id,
            time,
            timeTableItem.lessonNumber,
            isIconDisplayed = isItemLast
        )
        timeTableViewModel.updateTimeTableItem(newTimeTableItem)
    }

    private fun getTimeValue(time: String): String {
        val result = if(time.length == 1) {
            "0$time"
        } else {
            time
        }
        return result
    }

    private fun adapterOnLongClick(view: View, timeTableItem: TimeTableItem, position: Int) {
        showContextMenu(view, R.menu.edit_menu, timeTableItem, position)
    }

    private fun showContextMenu(
        v: View,
        @MenuRes menuRes: Int,
        timeTableItem: TimeTableItem,
        position: Int
    ) {
        val popup = PopupMenu(context, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.edit_option -> {
                    showEditTimePicker(timeTableItem, position)
                    true
                }
                else -> {
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
        popup.show()
    }
}