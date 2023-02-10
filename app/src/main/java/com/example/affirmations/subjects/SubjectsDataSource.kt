package com.example.affirmations.subjects

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.affirmations.data.subjectsList
import com.example.affirmations.model.Subject

class SubjectsDataSource (resources: Resources) {
    private val initialSubjectsList = subjectsList(resources)
    private val subjectsLiveData = MutableLiveData(initialSubjectsList)

    fun addSubject(subject: Subject) {
        val currentList = subjectsLiveData.value
        if (currentList == null) {
            subjectsLiveData.postValue(listOf(subject))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, subject)
            subjectsLiveData.postValue(updatedList)
        }
    }

    fun removeSubject(subject: Subject) {
        val currentList = subjectsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(subject)
            subjectsLiveData.postValue(updatedList)
        }
    }

    fun getSubjectForId(id: Long): Subject? {
        subjectsLiveData.value?.let { subjects ->
            return subjects.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getSubjects(): LiveData<List<Subject>> {
        return subjectsLiveData
    }


    companion object {
        private var INSTANCE: SubjectsDataSource? = null

        fun getDataSource(resources: Resources): SubjectsDataSource {
            return synchronized(SubjectsDataSource::class) {
                val newInstance = INSTANCE ?: SubjectsDataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}