package com.example.affirmations.subjects

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.affirmations.data.ScheduleDatabase
import com.example.affirmations.model.ScheduleItem
import com.example.affirmations.model.Subject
import com.example.affirmations.repository.ScheduleRepository
import com.example.affirmations.repository.SubjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SubjectsViewModel (application: Application): AndroidViewModel(application) {

    private val _inputName = MutableLiveData<String>()
    val inputName: LiveData<String> get() = _inputName

    //setting data from dialogs
    fun setSubjectName(text: String) {
        _inputName.value = text
    }

    val readSubjectsData: LiveData<List<Subject>>
    private val subjectsRepository: SubjectsRepository

    init {
        val userDao = ScheduleDatabase.getDatabase(
            application
        ).scheduleDao()
        subjectsRepository = SubjectsRepository(userDao)
        readSubjectsData = subjectsRepository.readSubjectsData

//        subjectsRepository = SubjectsRepository(userDao)
//        readSubjectsName = subjectsRepository.readSubjectsName
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