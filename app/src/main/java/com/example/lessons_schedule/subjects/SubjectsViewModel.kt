package com.example.lessons_schedule.subjects

import android.app.Application
import androidx.lifecycle.*
import com.example.lessons_schedule.data.ScheduleDatabase
import com.example.lessons_schedule.data.model.Subject
import com.example.lessons_schedule.data.repository.SubjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubjectsViewModel (application: Application): AndroidViewModel(application) {

    val readSubjectsData: LiveData<List<Subject>>
    private val subjectsRepository: SubjectsRepository

    init {
        val userDao = ScheduleDatabase.getDatabase(
            application
        ).scheduleDao()
        subjectsRepository = SubjectsRepository(userDao)
        readSubjectsData = subjectsRepository.readSubjectsData
    }

    fun addSubject(subject: Subject){
        viewModelScope.launch(Dispatchers.IO) {
            subjectsRepository.addSubject(subject)
        }
    }

    fun updateSubject(subject: Subject){
        viewModelScope.launch(Dispatchers.IO) {
            subjectsRepository.updateSubject(subject)
        }
    }

    fun deleteSubject(subject: Subject){
        viewModelScope.launch(Dispatchers.IO) {
            subjectsRepository.deleteSubject(subject)
        }
    }



}