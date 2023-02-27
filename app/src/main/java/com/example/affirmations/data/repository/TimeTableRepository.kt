package com.example.affirmations.data.repository

import androidx.lifecycle.LiveData
import com.example.affirmations.data.ScheduleDao
import com.example.affirmations.data.model.TimeTableItem

class TimeTableRepository (private val scheduleDao: ScheduleDao) {

    val readTimeTableData: LiveData<List<TimeTableItem>> = scheduleDao.readTimeTableData()

    suspend fun addTimeTableItem(timeTableItem: TimeTableItem){
        scheduleDao.addTimeTableItem(timeTableItem)
    }

    suspend fun updateTimeTableItem(timeTableItem: TimeTableItem){
        scheduleDao.updateTimeTableItem(timeTableItem)
    }

    suspend fun deleteTimeTableItem(timeTableItem: TimeTableItem){
        scheduleDao.deleteTimeTableItem(timeTableItem)
    }

}