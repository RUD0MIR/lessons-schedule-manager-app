package com.example.affirmations.schedule

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.affirmations.data.scheduleList
import com.example.affirmations.model.ScheduleItem

class ScheduleDataSource(resources: Resources) {
    private val initialScheduleList = scheduleList(resources)
    private val scheduleLiveData = MutableLiveData(initialScheduleList)

    fun addScheduleItem(scheduleItem: ScheduleItem) {
        val currentList = scheduleLiveData.value
        if (currentList == null) {
            scheduleLiveData.postValue(listOf(scheduleItem))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, scheduleItem)
            scheduleLiveData.postValue(updatedList)
        }
    }

    fun removeScheduleItem(scheduleItem: ScheduleItem) {
        val currentList = scheduleLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(scheduleItem)
            scheduleLiveData.postValue(updatedList)
        }
    }

    fun getScheduleItemForId(id: Long): ScheduleItem? {
        scheduleLiveData.value?.let { scheduleItems ->
            return scheduleItems.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getScheduleList(): LiveData<List<ScheduleItem>> {
        return scheduleLiveData
    }


    companion object {
        private var INSTANCE: ScheduleDataSource? = null

        fun getDataSource(resources: Resources): ScheduleDataSource {
            return synchronized(ScheduleDataSource::class) {
                val newInstance = INSTANCE ?: ScheduleDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}