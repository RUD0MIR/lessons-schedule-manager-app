package com.example.lessons_schedule.time_table

import android.icu.util.Calendar
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
import com.example.lessons_schedule.time_table.components.DeleteTimeTableDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


private const val TAG = "TimeTableActivity"

class TimeTableActivity : FragmentActivity() {

    private val context = this@TimeTableActivity

    private lateinit var binding: ActivityTimeTableBinding
    private lateinit var adapter: TimeTableAdapter
    private lateinit var model: TimeTableViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Recycler view and data
        adapter = TimeTableAdapter(
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
        recyclerView.adapter = adapter

        model = ViewModelProvider(context)[TimeTableViewModel::class.java]
        model.readTimeTableData.observe(context, Observer { timeTableItems ->
            adapter.submitList(timeTableItems)
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

    private fun onIconClick(timeTableItem: TimeTableItem) {
        val deleteDialog = DeleteTimeTableDialog(timeTableItem) {model.showIconOnLastItem()}
        deleteDialog.show(supportFragmentManager, DeleteTimeTableDialog.TAG)
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

   private fun showAddTimePicker() {
        val isSystem24Hour = DateFormat.is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(currentHour)
            .setMinute(currentMinute)
            .setTitleText(getString(R.string.add_time_table_item_dialog_title))
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setPositiveButtonText(getString(R.string.add_option))
            .setNegativeButtonText(getString(R.string.cancel_option))
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = model.getTimeValue(timePicker.hour.toString())
            val minute = model.getTimeValue(timePicker.minute.toString())
            val time = getString(R.string.time_sample, hour, minute)
            val lessonNumber = adapter.itemCount + 1

            val timeTableItem = TimeTableItem(
                0,
                time,
                lessonNumber,
                isIconDisplayed = true
            )
            model.addTimeTableItem(timeTableItem)

            model.hideIconOnPenultimateItem()
        }
       timePicker.show(supportFragmentManager, "AddTimePicker")
    }

    private fun showEditTimePicker(timeTableItem: TimeTableItem, position: Int) {
        val isSystem24Hour = DateFormat.is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        var hour = timeTableItem.lessonTime.substring(0, 2)
        var minute = timeTableItem.lessonTime.substring(3)

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setTitleText(getString(R.string.edit_time_table_item_dialog_title))
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .setPositiveButtonText(getString(R.string.save_option))
                .setNegativeButtonText(getString(R.string.cancel_option))
                .setHour(hour.toInt())
                .setMinute(minute.toInt())
                .build()

        timePicker.addOnPositiveButtonClickListener {
            hour = model.getTimeValue(timePicker.hour.toString())
            minute = model.getTimeValue(timePicker.minute.toString())

            val time = getString(R.string.time_sample, hour, minute)
            val isItemLast = position == adapter.itemCount - 1

            val newTimeTableItem = TimeTableItem(
                timeTableItem.id,
                time,
                timeTableItem.lessonNumber,
                isIconDisplayed = isItemLast
            )
            model.updateTimeTableItem(newTimeTableItem)
        }

        timePicker.show(supportFragmentManager, "EditTimePicker")
    }
}