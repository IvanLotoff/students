package ru.ivan.students.dto.request

data class CVRequest(
    val nameCV: String,
    val aboutInfo: String,
    val school: String,
    val university: String,
    val workStatus: String,
    val citizenship: String,
    val language: String,
    val workSchedule: String,
    val skill: String,
    val busyness : String
)
