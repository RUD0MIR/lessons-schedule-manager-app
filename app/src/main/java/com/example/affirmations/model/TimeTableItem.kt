package com.example.affirmations.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "time_table_item")
data class TimeTableItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "lesson_number")
    val lessonNumber: String,
    @ColumnInfo(name = "lesson_time")
    val lessonTime: String
) : Parcelable