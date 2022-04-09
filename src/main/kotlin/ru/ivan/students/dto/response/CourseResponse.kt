package ru.ivan.students.dto.response

data class CourseResponse(
    val id: String? = null,
    val name: String,
    val spec: String,
    val seatsNumber: Int,
    val teacher: String,
    val source: String,
)
