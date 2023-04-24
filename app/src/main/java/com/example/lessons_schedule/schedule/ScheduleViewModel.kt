package com.example.lessons_schedule.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lessons_schedule.R
import com.example.lessons_schedule.data.ScheduleDatabase
import com.example.lessons_schedule.data.model.ScheduleItem
import com.example.lessons_schedule.data.model.TimeTableItem
import com.example.lessons_schedule.data.repository.ScheduleRepository
import com.example.lessons_schedule.data.repository.SubjectsRepository
import com.example.lessons_schedule.data.repository.TimeTableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class ScheduleViewModel (application: Application): AndroidViewModel(application) {
    val readScheduleData: LiveData<List<ScheduleItem>>
    private val scheduleRepository: ScheduleRepository

    val readSubjectsName: LiveData<List<String>>
    private val subjectsRepository: SubjectsRepository

    val readTimeTableData: LiveData<List<TimeTableItem>>
    private val timeTableRepository: TimeTableRepository

    private val _weekState = MutableLiveData<Int>(ScheduleActivity.UPPER_WEEK)
    val weekState: LiveData<Int> get() = _weekState
    fun setWeekState(weekState: Int) {
        _weekState.value = weekState
    }

    fun getLessonNumberFromText(lessonTimeText: String): Int {
        return lessonTimeText.substring(0, 1).toInt()
    }
    suspend fun getLessonTimeByLessonNumber(lessonNumber: Int): String {
          return timeTableRepository.getLessonTimeByLessonNumber(lessonNumber)
    }

    fun changeDisabledState(scheduleItem: ScheduleItem) {
        val newScheduleItem = ScheduleItem(
            id = scheduleItem.id,
            subjectName = scheduleItem.subjectName,
            lessonTime = scheduleItem.lessonTime,
            number = scheduleItem.number,
            dayOfWeek = scheduleItem.dayOfWeek,
            isDisable = !scheduleItem.isDisable,
            weekState = scheduleItem.weekState,
            classroom = scheduleItem.classroom
        )
        updateScheduleItem(newScheduleItem)
    }

    fun insertDataToDatabase(
        subject: String,
        lessonTime: String,
        lessonNumber: Int,
        dayOfWeek: String,
        weekState: Int,
        classroom: String
    ) {
        val scheduleItem = ScheduleItem(
            id = 0,
            subjectName = subject,
            lessonTime = lessonTime,
            number = lessonNumber,
            dayOfWeek = dayOfWeek,
            weekState = weekState,
            classroom = classroom
        )
        addScheduleItem(scheduleItem)
    }

    fun updateDataInDatabase(
        subject: String,
        lessonTime: String,
        lessonNumber: Int,
        scheduleItem: ScheduleItem,
        weekState: Int,
        classroom: String
    ) {
        val newScheduleItem = ScheduleItem(
            id = scheduleItem.id,
            subjectName = subject,
            lessonTime = lessonTime,
            number = lessonNumber,
            dayOfWeek = scheduleItem.dayOfWeek,
            weekState = weekState,
            classroom = classroom
        )
        updateScheduleItem(newScheduleItem)
    }



    init {
        val userDao = ScheduleDatabase.getDatabase(
            application
        ).scheduleDao()

        scheduleRepository = ScheduleRepository(userDao)
        readScheduleData = scheduleRepository.readScheduleData

        subjectsRepository = SubjectsRepository(userDao)
        readSubjectsName = subjectsRepository.readSubjectsName


        timeTableRepository = TimeTableRepository(userDao)
        readTimeTableData = timeTableRepository.readTimeTableData
    }

    private fun addScheduleItem(scheduleItem: ScheduleItem){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.addScheduleItem(scheduleItem)
        }
    }

    private fun updateScheduleItem(scheduleItem: ScheduleItem){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.updateScheduleItem(scheduleItem)
        }
    }

    fun deleteScheduleItem(scheduleItem: ScheduleItem){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.deleteScheduleItem(scheduleItem)
        }
    }




}