package ru.ivan.students.dto.request

import ru.ivan.students.domian.Project
import ru.ivan.students.domian.Tag

data class ProjectRequest(
    val title: String,
    val description: String,
    val communication: String,
    var creatorId: String,
    val tags: List<TagRequest>
)

fun ProjectRequest.toEntity(idCreator: String): Project {
    val project = Project(
        title = this.title,
        description = this.description,
        communication = this.communication,
        creatorId = idCreator
    )
    project.tags = this.tags.toTagEntityList(project)
    return project
}

fun TagRequest.toTagEntity(projectProxy: Project): Tag {
    return Tag(
        name = this.name,
        about = this.about,
        project = projectProxy
    )
}

fun List<TagRequest>.toTagEntityList(projectProxy: Project): List<Tag> {
    return this.map {
        tagRequest -> tagRequest.toTagEntity(projectProxy)
    }
}