package com.example.affirmations.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.affirmations.data.DataSource
import com.example.affirmations.data.DaysOfWeek
import com.example.affirmations.data.TimeTableItem
import kotlin.random.Random

class TimeTableListViewModel (val dataSource: DataSource) : ViewModel() {

    val timeTableLiveData = dataSource.getTimeTableList()

    /* If the name and description are present, create new Flower and add it to the datasource */
    fun insertTimeTableItem(subject: String?, time: String?, number: Int, dayOfWeek: String) {
        if (subject == null || time == null || number == 0) {
            return
        }

        val newTimeTableItem = TimeTableItem(
            Random.nextLong(),
            subject,
            time,
            number,
            dayOfWeek
        )

        dataSource.addTimeTableItem(newTimeTableItem)
    }
}

class TimeTableListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeTableListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimeTableListViewModel(
                dataSource = DataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

