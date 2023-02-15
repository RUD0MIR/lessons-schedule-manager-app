package com.example.affirmations.time_table

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.affirmations.model.ScheduleItem
import com.example.affirmations.model.Subject
import com.example.affirmations.model.TimeTableItem
import com.example.affirmations.subjects.SubjectsDataSource
import com.example.affirmations.subjects.SubjectsViewModel
import kotlin.random.Random

class TimeTableViewModel (val dataSource: TimeTableDataSource) : ViewModel() {

    val timeTableLiveData = dataSource.getTimeTableItems()

    fun insertTimeTableItem(lessonNumber: String?, lessonTime: String?) {
        if (lessonNumber == null || lessonTime == null) {
            return
        }

        val newTimeTableItem = TimeTableItem(
            Random.nextLong(),
            lessonNumber,
            lessonTime
        )

        dataSource.addTimeTableItem(newTimeTableItem)
    }
}

class TimeTableViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeTableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimeTableViewModel(
                dataSource = TimeTableDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}