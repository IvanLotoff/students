package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.response.ProjectResponse
import ru.ivan.students.service.ProjectService

@RestController
@RequestMapping("/api/project/")
class ProjectController {

    @Autowired
    private lateinit var projectService: ProjectService

    @GetMapping("/get/id")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Вывод всех созданных проектов пользователем по аутентификации")
    fun getUserProjects(keycloakAuthenticationToken: KeycloakAuthenticationToken): List<ResponseEntity<ProjectResponse>> {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return projectService.getAllUserProjects(userId).map {
            ResponseEntity.ok(it)
        }
    }

    @GetMapping("/all")
    fun showAll(): List<ProjectResponse> = projectService.showAll()

    @PostMapping("/recommend")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Показать рекомендованные проекты")
    fun showRecommendedProjects(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
    ): List<ResponseEntity<ProjectResponse>> {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject

        return projectService.searchRecommendedProjects(userId).map {
            ResponseEntity.ok(it)
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Добавление проекта")
    fun addProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @RequestBody projectRequest: ProjectRequest
    ): ProjectResponse {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return projectService.addProject(projectRequest, userId)
    }

    @PostMapping("/search")
    @Operation(summary = "Поиск проекта")
    fun searchProject(
        @RequestParam key: String
    ): List<ResponseEntity<ProjectResponse>> {
        return projectService.searchProject(key).map {
            ResponseEntity.ok(it)
        }
    }

    @PostMapping("/removeLike/{idProject}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Поставить лайк на проект")
    fun removeLikeOnProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @PathVariable idProject: String
    ): ProjectResponse {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return projectService.deleteLikeProject(idProject, userId)
    }

    @PostMapping("/like/{idProject}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Поставить лайк на проект")
    fun likeProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @PathVariable idProject: String
    ): ProjectResponse {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return projectService.likeProject(idProject, userId)
    }

//    @PostMapping("/delete")
//    @PreAuthorize("hasRole('USER')")
//    @SecurityRequirement(name = "apiKey")
//    fun deleteProject(
//        keycloakAuthenticationToken: KeycloakAuthenticationToken,
//        @RequestBody id: String
//    ): ProjectResponse {
//        val userId =
//            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
//        return projectService.addProject(projectRequest, userId)
//    }

}