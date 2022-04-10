package ru.ivan.students.dto.response

data class ProjectResponse(
    val title: String,
    val description: String,
    val communication: String,
    var creatorId: String,
)
