package com.example.affirmations.repository

import androidx.lifecycle.LiveData
import com.example.affirmations.data.ScheduleDao
import com.example.affirmations.model.Subject

class SubjectsRepository (private val scheduleDao: ScheduleDao) {

    val readSubjectsData: LiveData<List<Subject>> = scheduleDao.readSubjectData()
    val readSubjectsName: LiveData<List<String>> = scheduleDao.readSubjectsName()

    suspend fun addSubject(subject: Subject){
        scheduleDao.addSubject(subject)
    }

    suspend fun updateSubject(subject: Subject){
        scheduleDao.updateSubject(subject)
    }

    suspend fun deleteSubject(subject: Subject){
        scheduleDao.deleteSubject(subject)
    }

}