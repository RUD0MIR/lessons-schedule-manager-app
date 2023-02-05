package com.example.affirmations.data

import android.content.res.Resources

fun timeTableList(resources: Resources): List<TimeTableItem> {
    var number = 1
    var id: Long = 1
    return listOf(
        TimeTableItem(id, "Разработка программных модулей", "8:00-9:30", number),
        TimeTableItem(id++, "Стандартизация, сертификация и техническое документоведение", "8:00-9:30", number++),
        TimeTableItem(id++, "Иностранный язык в проф деятельности", "8:00-9:30", number++),
        TimeTableItem(id++, "Разработка мобильных приложений", "8:00-9:30", number++)
    )
}