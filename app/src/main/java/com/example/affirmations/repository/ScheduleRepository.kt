package com.example.affirmations.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.affirmations.data.ScheduleDao
import com.example.affirmations.model.ScheduleItem

class ScheduleRepository (private val scheduleDao: ScheduleDao) {

    val readScheduleData: LiveData<List<ScheduleItem>> = scheduleDao.readScheduleData()
    val readSubjectsName: LiveData<List<String>> = scheduleDao.readSubjectsName()

    suspend fun addScheduleItem(scheduleItem: ScheduleItem){
        scheduleDao.addScheduleItem(scheduleItem)
    }

    suspend fun updateScheduleItem(scheduleItem: ScheduleItem){
        scheduleDao.updateScheduleItem(scheduleItem)
    }

    suspend fun deleteScheduleItem(scheduleItem: ScheduleItem){
        scheduleDao.deleteScheduleItem(scheduleItem)
    }
}