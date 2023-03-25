package com.example.lessons_schedule.data.repository

import androidx.lifecycle.LiveData
import com.example.lessons_schedule.data.ScheduleDao
import com.example.lessons_schedule.data.model.TimeTableItem

class TimeTableRepository (private val scheduleDao: ScheduleDao) {

    val readTimeTableData: LiveData<List<TimeTableItem>> = scheduleDao.readTimeTableData()
    val readLessonsTime: LiveData<List<String>> = scheduleDao.readLessonsTime()

    suspend fun addTimeTableItem(timeTableItem: TimeTableItem){
        scheduleDao.addTimeTableItem(timeTableItem)
    }

    suspend fun updateTimeTableItem(timeTableItem: TimeTableItem){
        scheduleDao.updateTimeTableItem(timeTableItem)
    }

    suspend fun deleteTimeTableItem(timeTableItem: TimeTableItem){
        scheduleDao.deleteTimeTableItem(timeTableItem)
    }

    suspend fun getLessonTimeByLessonNumber(lessonNumber: Int): String {
        return scheduleDao.getLessonTimeByLessonNumber(lessonNumber)
    }
}