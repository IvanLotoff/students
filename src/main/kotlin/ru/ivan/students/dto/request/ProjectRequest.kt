package ru.ivan.students.dto.request

import ru.ivan.students.domian.Project

data class ProjectRequest(
    val title: String,
    val description: String,
    val communication: String,
    var creatorId: String,
)

fun ProjectRequest.toEntity(idCreator: String) = Project(
    title = this.title,
    description = this.description,
    communication = this.communication,
    creatorId = idCreator
)