package com.example.affirmations.model


data class ScheduleItem(
        val id: Long,
        var subject: String,
        var time: String,
        var number: Int,
        var dayOfWeek: String,
        var isDisable: Boolean = false
)