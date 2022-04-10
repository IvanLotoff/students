package ru.ivan.students.dto.request

import ru.ivan.students.domian.Project
import ru.ivan.students.mapper.ProjectConverter

data class ProjectRequest(
    val title: String,
    val description: String,
    val communication: String,
    var creatorId: String,
    val tags: List<TagRequest>
)

fun ProjectRequest.toEntity(idCreator: String): Project {
    val converter: ProjectConverter = ProjectConverter()
    var pr = Project(
        title = this.title,
        description = this.description,
        communication = this.communication,
        creatorId = idCreator

    )

    //pr.tags = converter.toListOfTagToRequest(pr.id!!, this.tags)

    return pr
}