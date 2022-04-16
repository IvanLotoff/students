package ru.ivan.students.dto.request

import ru.ivan.students.domian.Course
import ru.ivan.students.service.CourseService

data class CourseRequest(
    val name: String,
    val about: String,
    val source: String,
)

fun CourseRequest.toEntity(userId: String?): Course {
    return Course(
        name = this.name,
        about = this.about,
        source = this.source,
        userId = userId
    )
}
