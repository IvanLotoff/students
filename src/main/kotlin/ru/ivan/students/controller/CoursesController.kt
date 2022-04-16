package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.dto.request.CourseRequest
import ru.ivan.students.dto.response.CourseResponse
import ru.ivan.students.service.CourseService

@RestController
@RequestMapping("api/courses")
class CoursesController {
    @Autowired
    private lateinit var courseService: CourseService

    @PostMapping("/addCourse")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Добавляем курс для пользователя")
    fun addCourse(
        @RequestBody courseRequest: CourseRequest,
        keycloakAuthenticationToken: KeycloakAuthenticationToken
    ): CourseResponse {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return courseService.addCourse(courseRequest, userId)
    }

    @GetMapping("user/all")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Выводим все курсы пользователя")
    fun addAllCourses(keycloakAuthenticationToken: KeycloakAuthenticationToken): List<CourseResponse> {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return courseService.findAllByUserId(userId)
    }

    @GetMapping("/byUserId/{id}")
    @Operation(summary = "Выводим все курсы пользователя по его id. Не требует аутентификации")
    fun findAllCoursesByUserId(@PathVariable id: String): List<CourseResponse> {
        return courseService.findAllByUserId(id)
    }

    @GetMapping("/all")
    @Operation(summary = "Выводим все курсы пользователей")
    fun findAllCourses(): List<CourseResponse> {
        return courseService.showAll()
    }
}