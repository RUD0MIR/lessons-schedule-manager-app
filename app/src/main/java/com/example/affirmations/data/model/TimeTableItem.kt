package com.example.affirmations.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "time_table_item")
data class TimeTableItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "lesson_time")
    val lessonTime: String,
    var isIconDisplayed: Boolean = false
)