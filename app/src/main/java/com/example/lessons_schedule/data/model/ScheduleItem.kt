package com.example.lessons_schedule.data.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "schedule_item")
data class ScheduleItem(
        @PrimaryKey(autoGenerate = true)
        val id: Int,

        @ColumnInfo(name = "subject_name")
        val subjectName: String,

        @ColumnInfo(name = "lesson_time")
        val lessonTime: String,

        val number: Int,

        @ColumnInfo(name = "day_of_week")
        var dayOfWeek: String,

        @ColumnInfo(name = "is_disable")
        var isDisable: Boolean = false
): Parcelable