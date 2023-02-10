package com.example.affirmations.subjects

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.affirmations.model.Subject
import kotlin.random.Random

class SubjectsViewModel (val dataSource: SubjectsDataSource) : ViewModel() {

    val subjectsLiveData = dataSource.getSubjects()

    fun insertSubject(subject: String?) {
        if (subject == null) {
            return
        }

        val newSubject = Subject(
            Random.nextLong(),
            subject
        )

        dataSource.addSubject(newSubject)
    }
}

class SubjectViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubjectsViewModel(
                dataSource = SubjectsDataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}