package ru.ivan.students.dto.response

data class ProjectResponse(
    val id: String,
    val title: String,
    val description: String,
    val communication: String,
    var creatorId: String,
    var tags: List<TagResponse>
)
