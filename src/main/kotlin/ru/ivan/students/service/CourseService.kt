package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.toResponse
import ru.ivan.students.dto.request.CourseRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.dto.response.CourseResponse
import ru.ivan.students.repository.CourseRepository

@Service
class CourseService {
    @Autowired
    private lateinit var courseRepository: CourseRepository

    fun addCourse(course: CourseRequest, userId: String): CourseResponse {
        return courseRepository.save(course.toEntity(userId)).toResponse()
    }

    fun findAllByUserId(userId: String): List<CourseResponse> {
        return courseRepository.findAllByUserId(userId).map { course -> course.toResponse() }
    }

    fun showAll() = courseRepository.findAll().map { it -> it.toResponse() }
}