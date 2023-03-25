package com.example.lessons_schedule.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lessons_schedule.data.model.ScheduleItem
import com.example.lessons_schedule.data.model.Subject
import com.example.lessons_schedule.data.model.TimeTableItem


@Dao
interface ScheduleDao {

    //schedule items queries
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addScheduleItem(scheduleItem: ScheduleItem)

    @Update
    suspend fun updateScheduleItem(scheduleItem: ScheduleItem)

    @Delete
    suspend fun deleteScheduleItem(scheduleItem: ScheduleItem)

    @Query("SELECT * FROM schedule_item ORDER BY number ASC")
    fun readScheduleData(): LiveData<List<ScheduleItem>>

    //subjects queries
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSubject(subject: Subject)

    @Update
    suspend fun updateSubject(subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Query("SELECT name FROM subject ORDER BY id ASC")
    fun readSubjectsName(): LiveData<List<String>>

    @Query("SELECT * FROM subject ORDER BY id ASC")
    fun readSubjectData(): LiveData<List<Subject>>

    //timeTable queries
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTimeTableItem(timeTableItem: TimeTableItem)

    @Update
    suspend fun updateTimeTableItem(timeTableItem: TimeTableItem)

    @Delete
    suspend fun deleteTimeTableItem(timeTableItem: TimeTableItem)

    @Query("SELECT * FROM time_table_item ORDER BY id ASC")
    fun readTimeTableData(): LiveData<List<TimeTableItem>>

    @Query("SELECT lesson_time FROM time_table_item ORDER BY id ASC")
    fun readLessonsTime(): LiveData<List<String>>

    @Query("SELECT lesson_time FROM time_table_item WHERE lesson_number = :lessonNumber")
    suspend fun getLessonTimeByLessonNumber(lessonNumber: Int): String

}