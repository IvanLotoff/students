package ru.ivan.students.dto.request

data class ProjectRequest(
    val interests: String,
    val tag: String,
    val skills: String,
    val description: String,
    val telegram: String,
)
