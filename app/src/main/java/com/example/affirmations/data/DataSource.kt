package com.example.affirmations.data

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataSource(resources: Resources) {
    private val initialTimeTableList = timeTableList(resources)
    private val timeTableLiveData = MutableLiveData(initialTimeTableList)

    /* Adds items to liveData and posts value. */
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

    /* Removes flower from liveData and posts value. */
    fun removeTimeTableItem(timeTableItem: TimeTableItem) {
        val currentList = timeTableLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(timeTableItem)
            timeTableLiveData.postValue(updatedList)
        }
    }

    /* Returns flower given an ID. */
    fun getTimeTableItemForId(id: Long): TimeTableItem? {
        timeTableLiveData.value?.let { timeTableItems ->
            return timeTableItems.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getTimeTableList(): LiveData<List<TimeTableItem>> {
        return timeTableLiveData
    }


    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}