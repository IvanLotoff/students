package ru.ivan.students.dto.request

import ru.ivan.students.domian.Project
import ru.ivan.students.domian.Tag

data class ProjectRequest(
    val title: String,
    val description: String,
    val communication: String,
    val tags: MutableList<TagRequest>
)

fun ProjectRequest.toEntity(idCreator: String, projectId: String? = null): Project {
    val project = Project(
        id = projectId,
        title = this.title,
        description = this.description,
        communication = this.communication,
        creatorId = idCreator
    )
    project.tags = this.tags.toTagEntityList(project) as MutableList<Tag>
    return project
}

fun List<TagRequest>.toTagEntityList(projectProxy: Project): List<Tag> {
    return this.map {
        tagRequest -> tagRequest.toEntity(projectProxy)
    }
}