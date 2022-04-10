package ru.ivan.students.controller

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
    fun addProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @RequestBody projectRequest: ProjectRequest
    ): ProjectResponse {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return projectService.addProject(projectRequest, userId)
    }


    @PostMapping("/like")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    fun likeProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @RequestBody idProject: String
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