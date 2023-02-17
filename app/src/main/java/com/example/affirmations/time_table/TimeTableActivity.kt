package com.example.affirmations.time_table

import android.os.Bundle
import android.text.InputFilter
import android.text.format.DateFormat
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.affirmations.R
import com.example.affirmations.adapter.TimeTableAdapter
import com.example.affirmations.databinding.ActivityTimeTableBinding
import com.example.affirmations.databinding.TimeTableDialogItemBinding
import com.example.affirmations.model.TimeTableItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private lateinit var dialogItemBinding: TimeTableDialogItemBinding
    private lateinit var timeTableAdapter: TimeTableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeTableBinding.inflate(layoutInflater)
        dialogItemBinding = TimeTableDialogItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //input block
        val filter =
            InputFilter { source, start, end, dest, dstart, dend ->
                    if (source[0].digitToInt() > 2) {
                        return@InputFilter ""
                }
                null
            }
        dialogItemBinding.hourTf.editText?.filters = arrayOf(filter)

        //Recycler view and data
        timeTableAdapter = TimeTableAdapter(
            context = context
        ) { itemView, timeTableItem, position -> adapterOnLongClick(itemView, timeTableItem, position) }

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
            showAddTimePicker()
        }

    }


    //TODO:not for db
    private fun showEditTimePicker() {
        val isSystem24Hour = DateFormat.is24HourFormat(context)
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker =
            MaterialTimePicker.Builder()
                .setTheme(R.style.timePickerTheme)
                .setTimeFormat(clockFormat)
                .setTitleText(getString(R.string.edit_time_table_item_dialog_title))
                .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
                .setPositiveButtonText(getString(R.string.save_option))
                .setNegativeButtonText(getString(R.string.cancel_option))
                .build()
        picker.show(supportFragmentManager, "tag")
    }

    //TODO:not for db
    private fun showAddTimePicker() {
        val isSystem24Hour = DateFormat.is24HourFormat(context)
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

    //TODO:not for db
    private fun showDeleteTimeTableItemDialog(timeTableItem: TimeTableItem, position: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle(resources.getString(R.string.delete_lesson_dialog_title))
            .setNeutralButton(resources.getString(R.string.cancel_option)) { _, _ -> }
            .setPositiveButton(resources.getString(R.string.delete_option)) { _, _ ->
                timeTableViewModel.dataSource.removeTimeTableItem(timeTableItem)
                timeTableAdapter.notifyItemChanged(position)
            }
            .show()
    }

    private fun adapterOnLongClick(view: View, timeTableItem: TimeTableItem, position: Int) {
        showContextMenu(view, R.menu.edit_delete_menu, timeTableItem, position)
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
                    showEditTimePicker()
                    true
                }
                R.id.delete_option -> {
                    showDeleteTimeTableItemDialog(timeTableItem, position)
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