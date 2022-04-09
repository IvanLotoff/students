package ru.ivan.students.dto.request

import ru.ivan.students.domian.Course
import ru.ivan.students.service.CourseService

data class CourseRequest(
    val name: String,
    val spec: String,
    val seatsNumber: Int,
    val teacher: String,
    val source: String,
)

fun CourseRequest.toEntity(userId: String): Course {
    return Course(
        name = this.name,
        spec = this.spec,
        seatsNumber = this.seatsNumber,
        teacher = this.teacher,
        source = this.source,
        userId = userId
    )
}
