package com.example.lessons_schedule.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "time_table_item")
data class TimeTableItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "lesson_time")
    val lessonTime: String,//"00:00"
    @ColumnInfo(name = "lesson_number")
    val lessonNumber: Int,//1
    //@ColumnInfo(name = "is_icon_displayed")
    var isIconDisplayed: Boolean = false
)