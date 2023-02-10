package com.example.affirmations.model


data class ScheduleItem(
        val id: Long,
        val subject: String,
        val time: String,
        val number: Int,
        val dayOfWeek: String
)