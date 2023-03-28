package com.example.lessons_schedule.time_table

import android.app.Application
import androidx.lifecycle.*
import com.example.lessons_schedule.data.ScheduleDatabase
import com.example.lessons_schedule.data.model.TimeTableItem
import com.example.lessons_schedule.data.repository.TimeTableRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun hideIconOnPenultimateItem() {
        val currentList = readTimeTableData.value
            if(!currentList.isNullOrEmpty()) {
                val penultimateItem = currentList[currentList.size - 1]
                val newItem = TimeTableItem(
                    penultimateItem.id,
                    penultimateItem.lessonTime,
                    penultimateItem.lessonNumber,
                    isIconDisplayed = false
                )
                updateTimeTableItem(newItem)
            }
    }

    fun showIconOnLastItem() {
        val currentList = readTimeTableData.value
            if(!currentList.isNullOrEmpty() && currentList.size > 1) {
                val lastItem = currentList[currentList.size - 2]
                val newItem = TimeTableItem(
                    lastItem.id,
                    lastItem.lessonTime,
                    lastItem.lessonNumber,
                    isIconDisplayed = true
                )
                updateTimeTableItem(newItem)
            }
    }

    fun getTimeValue(time: String): String {
        val result = if(time.length == 1) {
            "0$time"
        } else {
            time
        }
        return result
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