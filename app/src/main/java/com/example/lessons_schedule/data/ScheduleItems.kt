package com.example.lessons_schedule.data

//fun scheduleList(resources: Resources): List<ScheduleItem> {
//        return mutableListOf(
//                ScheduleItem(
//                        id,
//                        "Разработка программных модулей",
//                        timeMap.getValue(3),
//                        3,
//                        daysOfWeek()[0],
//                        true
//                ),
//                ScheduleItem(
//                        id++,
//                        "Стандартизация, сертификация и техническое документоведение",
//                        timeMap.getValue(4),
//                        4,
//                        daysOfWeek()[0],
//                        true
//                ),
//                ScheduleItem(
//                        id++,
//                        "Иностранный язык в проф деятельности",
//                        timeMap.getValue(5),
//                        5,
//                        daysOfWeek()[0]
//                ),
//                ScheduleItem(
//                        id++,
//                        "Разработка мобильных приложений",
//                        timeMap.getValue(6),
//                        6,
//                        daysOfWeek()[0]
//                ),
//
//                ScheduleItem(id++,
//                        "Основы философии",
//                        timeMap.getValue(4),
//                        4,
//                        daysOfWeek()[2]
//                ),
//                ScheduleItem(id++,
//                        "Физическая культура",
//                        timeMap.getValue(5),
//                        5,
//                        daysOfWeek()[2]
//                ),
//
//                ScheduleItem(
//                        id++,
//                        "Разработка программных модулей",
//                        timeMap.getValue(1),
//                        1,
//                        daysOfWeek()[3]
//                ),
//                ScheduleItem(
//                        id++,
//                        "Разработка программных модулей",
//                        timeMap.getValue(2),
//                        2,
//                        daysOfWeek()[3]
//                ),
//                ScheduleItem(
//                        id++,
//                        "Системное программирование",
//                        timeMap.getValue(3),
//                        3,
//                        daysOfWeek()[3]
//                ),
//
//                ScheduleItem(
//                        id++,
//                        "Стандартизация, сертификация и техническое документоведение",
//                        timeMap.getValue(2),
//                        2,
//                        daysOfWeek()[4]
//                ),
//                ScheduleItem(
//                        id++,
//                        "Разработка мобильных приложений",
//                        timeMap.getValue(3),
//                        3,
//                        daysOfWeek()[4]
//                ),
//                ScheduleItem(
//                        id++,
//                        "Разработка мобильных приложений",
//                        timeMap.getValue(4),
//                        4,
//                        daysOfWeek()[4]
//                ),
//
//                ScheduleItem(
//                        id++,
//                        "Системное программирование",
//                        timeMap.getValue(1),
//                        1,
//                        daysOfWeek()[5]
//                ),
//                ScheduleItem(
//                        id++,
//                        "Технология разработки программного обеспечения",
//                        timeMap.getValue(2),
//                        2,
//                        daysOfWeek()[5]
//                ),
//                ScheduleItem(
//                        id++,
//                        "Разработка мобильных приложений",
//                        timeMap.getValue(3),
//                        3,
//                        daysOfWeek()[5]
//                )
//        )
//}

fun daysOfWeek() : List<String> {
        return listOf("Пн","Вт","Ср","Чт","Пт","Сб")
}

//fun subjectsList(resources: Resources): List<Subject> {
////        var id: Long = 0
////        return mutableListOf(
////                Subject(id, "Стандартизация, сертификация и техническое документоведение"),
////                Subject(id++, "Иностранный язык в проф деятельности"),
////                Subject(id++, "Разработка мобильных приложений"),
////                Subject(id++, "Основы философии"),
////                Subject(id++, "Разработка программных модулей"),
////                Subject(id++, "Системное программирование"),
////                Subject(id++, "Физкультура"),
////                Subject(id++, "Технология разработки программного обеспечения")
////        )
//}
//temp
//fun subjectArray(): Array<Subject> {
//        var id: Long = 0
//        return arrayOf(
//                Subject(id, "Стандартизация, сертификация и техническое документоведение"),
//                Subject(id++, "Иностранный язык в проф деятельности"),
//                Subject(id++, "Разработка мобильных приложений"),
//                Subject(id++, "Основы философии"),
//                Subject(id++, "Разработка программных модулей"),
//                Subject(id++, "Системное программирование"),
//                Subject(id++, "Физкультура"),
//                Subject(id++, "Технология разработки программного обеспечения")
//        )
//}

val timeMap =  mapOf(
        Pair(1, "8:00-9:30"),
        Pair(2, "09:40-11:10"),
        Pair(3, "11:20-12:50"),
        Pair(4, "13:20-14:50"),
        Pair(5, "15:00-16:30"),
        Pair(6, "16:40-18:10")
)

//fun timeTableList(resources: Resources): List<TimeTableItem> {
//        return mutableListOf(
//                TimeTableItem(
//                        id = Random.nextLong(),
//                        lessonNumber = "1 урок",
//                        lessonTime = "08:00-09:40"
//                ),
//                TimeTableItem(
//                        id = Random.nextLong(),
//                        lessonNumber = "2 урок",
//                        lessonTime = "09:40-11:10",
//                ),
//                TimeTableItem(
//                        id = Random.nextLong(),
//                        lessonNumber = "3 урок",
//                        lessonTime = "11:20-12:50",
//                ),
//                TimeTableItem(
//                        id = Random.nextLong(),
//                        lessonNumber = "4 урок",
//                        lessonTime = "13:20-14:50",
//                ),
//                TimeTableItem(
//                        id = Random.nextLong(),
//                        lessonNumber = "5 урок",
//                        lessonTime = "15:00-16:30",
//                ),
//                TimeTableItem(
//                        id = Random.nextLong(),
//                        lessonNumber = "6 урок",
//                        lessonTime = "16:40-18:10",
//                )
//        )
//}
