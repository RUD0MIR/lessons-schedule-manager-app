package com.example.affirmations.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.affirmations.data.ScheduleDatabase
import com.example.affirmations.data.model.ScheduleItem
import com.example.affirmations.data.repository.ScheduleRepository
import com.example.affirmations.data.repository.SubjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleViewModel (application: Application): AndroidViewModel(application) {

    val readScheduleData: LiveData<List<ScheduleItem>>
    private val scheduleRepository: ScheduleRepository

    val readSubjectsName: LiveData<List<String>>
    private val subjectsRepository: SubjectsRepository

    init {
        val userDao = ScheduleDatabase.getDatabase(
            application
        ).scheduleDao()
        scheduleRepository = ScheduleRepository(userDao)
        readScheduleData = scheduleRepository.readScheduleData

        subjectsRepository = SubjectsRepository(userDao)
        readSubjectsName = subjectsRepository.readSubjectsName
    }

    fun addScheduleItem(scheduleItem: ScheduleItem){
        viewModelScope.launch(Dispatchers.IO) {
            scheduleRepository.addScheduleItem(scheduleItem)
        }
    }

    fun updateScheduleItem(scheduleItem: ScheduleItem){
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