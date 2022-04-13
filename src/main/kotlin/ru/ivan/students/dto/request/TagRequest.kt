package ru.ivan.students.dto.request

import ru.ivan.students.domian.Project
import ru.ivan.students.domian.Tag

/** Принимает инфу от пользователя
 *
 */
data class TagRequest(
    val name: String,
    val about: String,
)

/**
 * Здесь пробрасывается projectProxy, эта сущность Project
 * с лишь установленным id
 */
fun TagRequest.toEntity(projectProxy: Project): Tag {
    return Tag(
        name = this.name,
        about = this.about,
        project = projectProxy
    )
}
