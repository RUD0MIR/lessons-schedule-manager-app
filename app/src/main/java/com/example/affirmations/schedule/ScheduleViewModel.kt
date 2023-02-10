package com.example.affirmations.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.affirmations.model.ScheduleItem
import kotlin.random.Random

class ScheduleViewModel (val dataSource: ScheduleDataSource) : ViewModel() {

    val scheduleLiveData = dataSource.getScheduleList()

    fun insertScheduleItem(subject: String?, time: String?, number: Int, dayOfWeek: String) {
        if (subject == null || time == null || number == 0) {
            return
        }

        val newScheduleItem = ScheduleItem(
            Random.nextLong(),
            subject,
            time,
            number,
            dayOfWeek
        )

        dataSource.addScheduleItem(newScheduleItem)
    }
}

class ScheduleViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScheduleViewModel(
                dataSource = ScheduleDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

