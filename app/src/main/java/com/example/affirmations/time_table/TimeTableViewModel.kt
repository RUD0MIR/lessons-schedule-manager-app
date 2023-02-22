package com.example.affirmations.time_table

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.affirmations.data.ScheduleDatabase
import com.example.affirmations.model.Subject
import com.example.affirmations.model.TimeTableItem
import com.example.affirmations.repository.SubjectsRepository
import com.example.affirmations.repository.TimeTableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class TimeTableViewModel (application: Application): AndroidViewModel(application) {

    val readTimeTableData: LiveData<List<TimeTableItem>>
    private val timeTableRepository: TimeTableRepository

    init {
        val userDao = ScheduleDatabase.getDatabase(
            application
        ).scheduleDao()
        timeTableRepository = TimeTableRepository(userDao)
        readTimeTableData = timeTableRepository.readTimeTableData

//        subjectsRepository = SubjectsRepository(userDao)
//        readSubjectsName = subjectsRepository.readSubjectsName
    }

    fun addTimeTableItem(timeTableItem: TimeTableItem){
        viewModelScope.launch(Dispatchers.IO) {
            timeTableRepository.addTimeTableItem(timeTableItem)
        }
    }

    fun updateTimeTableItem(timeTableItem: TimeTableItem){
        viewModelScope.launch(Dispatchers.IO) {
            timeTableRepository.updateTimeTableItem(timeTableItem)
        }
    }

    fun deleteTimeTableItem(timeTableItem: TimeTableItem){
        viewModelScope.launch(Dispatchers.IO) {
            timeTableRepository.deleteTimeTableItem(timeTableItem)
        }
    }

}