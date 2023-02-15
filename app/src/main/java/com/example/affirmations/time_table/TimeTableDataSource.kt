package com.example.affirmations.time_table

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.affirmations.data.timeTableList
import com.example.affirmations.model.Subject
import com.example.affirmations.model.TimeTableItem
import com.example.affirmations.subjects.SubjectsDataSource

class TimeTableDataSource (resources: Resources) {
    private val initialTimeTableList = timeTableList(resources)
    private val timeTableLiveData = MutableLiveData(initialTimeTableList)

    fun addTimeTableItem(timeTableItem: TimeTableItem) {
        val currentList = timeTableLiveData.value
        if (currentList == null) {
            timeTableLiveData.postValue(listOf(timeTableItem))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, timeTableItem)
            timeTableLiveData.postValue(updatedList)
        }
    }

    fun removeTimeTableItem(timeTableItem: TimeTableItem) {
        val currentList = timeTableLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(timeTableItem)
            timeTableLiveData.postValue(updatedList)
        }
    }

    fun getTimeTableItemForId(id: Long): TimeTableItem? {
        timeTableLiveData.value?.let { timeTableItems ->
            return timeTableItems.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getTimeTableItems(): LiveData<List<TimeTableItem>> {
        return timeTableLiveData
    }


    companion object {
        private var INSTANCE: TimeTableDataSource? = null

        fun getDataSource(resources: Resources): TimeTableDataSource {
            return synchronized(TimeTableDataSource::class) {
                val newInstance = INSTANCE ?: TimeTableDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}