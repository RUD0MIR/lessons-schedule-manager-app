package com.example.affirmations.data

import android.content.res.Resources
import javax.security.auth.Subject

fun timeTableList(resources: Resources): List<TimeTableItem> {
    var id: Long = 1
    //TODO: Extract time of classes into class
    //TODO: Remove days of week


        return mutableListOf(
                TimeTableItem(
                        id,
                        "Разработка программных модулей",
                        timeMap.getValue(3),
                        3,
                        daysOfWeek()[0]
                ),
                TimeTableItem(
                        id++,
                        "Стандартизация, сертификация и техническое документоведение",
                        timeMap.getValue(4),
                        4,
                        daysOfWeek()[0]
                ),
                TimeTableItem(
                        id++,
                        "Иностранный язык в проф деятельности",
                        timeMap.getValue(5),
                        5,
                        daysOfWeek()[0]
                ),
                TimeTableItem(
                        id++,
                        "Разработка мобильных приложений",
                        timeMap.getValue(6),
                        6,
                        daysOfWeek()[0]
                ),

                TimeTableItem(id++,
                        "Основы философии",
                        timeMap.getValue(4),
                        4,
                        daysOfWeek()[2]
                ),
                TimeTableItem(id++,
                        "Физическая культура",
                        timeMap.getValue(5),
                        5,
                        daysOfWeek()[2]
                ),

                TimeTableItem(
                        id++,
                        "Разработка программных модулей",
                        timeMap.getValue(1),
                        1,
                        daysOfWeek()[3]
                ),
                TimeTableItem(
                        id++,
                        "Разработка программных модулей",
                        timeMap.getValue(2),
                        2,
                        daysOfWeek()[3]
                ),
                TimeTableItem(
                        id++,
                        "Системное программирование",
                        timeMap.getValue(3),
                        3,
                        daysOfWeek()[3]
                ),

                TimeTableItem(
                        id++,
                        "Стандартизация, сертификация и техническое документоведение",
                        timeMap.getValue(2),
                        2,
                        daysOfWeek()[4]
                ),
                TimeTableItem(
                        id++,
                        "Разработка мобильных приложений",
                        timeMap.getValue(3),
                        3,
                        daysOfWeek()[4]
                ),
                TimeTableItem(
                        id++,
                        "Разработка мобильных приложений",
                        timeMap.getValue(4),
                        4,
                        daysOfWeek()[4]
                ),

                TimeTableItem(
                        id++,
                        "Системное программирование",
                        timeMap.getValue(1),
                        1,
                        daysOfWeek()[5]
                ),
                TimeTableItem(
                        id++,
                        "Технология разработки программного обеспечения",
                        timeMap.getValue(2),
                        2,
                        daysOfWeek()[5]
                ),
                TimeTableItem(
                        id++,
                        "Разработка мобильных приложений",
                        timeMap.getValue(3),
                        3,
                        daysOfWeek()[5]
                )
        )
}

fun daysOfWeek() : List<String> {
        return listOf("Пн","Вт","Ср","Чт","Пт","Сб")
}

val timeMap =  mapOf(
        Pair(1, "8:00-9:30"),
        Pair(2, "09:40-11:10"),
        Pair(3, "11:20-12:50"),
        Pair(4, "13:20-14:50"),
        Pair(5, "15:00-16:30"),
        Pair(6, "16:40-18:10")
)
