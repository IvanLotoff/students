package ru.ivan.students.dto.request

import ru.ivan.students.domian.Tag
import ru.ivan.students.mapper.ProjectConverter

/** Принимает инфу от пользователя
 *
 */
data class TagRequest(
    val name: String,
    val about: String,
)

fun TagRequest.toEntity(idProject: String): Tag {
    val converter: ProjectConverter = ProjectConverter()
    return Tag(
        id = idProject,
        name = this.name,
        about = this.about
    )
}
