package ru.ivan.students.controller

import com.google.gson.JsonObject
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.dto.request.ProjectRemoval
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.TagRequest
import ru.ivan.students.dto.response.ProjectResponse
import ru.ivan.students.dto.response.UserResponse
import ru.ivan.students.service.ProjectService

@RestController
@RequestMapping("/api/project/")
class ProjectController {

    @Autowired
    private lateinit var projectService: ProjectService

    @GetMapping("/getCreated")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Вывод всех созданных проектов пользователем по аутентификации")
    fun getCreatedProjects(keycloakAuthenticationToken: KeycloakAuthenticationToken): ResponseEntity<List<ProjectResponse>> {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return ResponseEntity.ok(projectService.getAllUserProjects(userId))
    }

    @GetMapping("/getCreated/{idUser}")
    @Operation(summary = "Вывод всех созданных проектов пользователем по id")
    fun getCreatedProjectsByID(@PathVariable idUser: String): ResponseEntity<List<ProjectResponse>> {
        return ResponseEntity.ok(projectService.getAllUserProjects(idUser))
    }

    @GetMapping("/all")
    @Operation(summary = "Вывод всех созданных проектов пользователями")
    fun showAll(): List<ProjectResponse> = projectService.showAll()

    @GetMapping("/recommend")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Показать рекомендованные проекты")
    fun showRecommendedProjects(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
    ): ResponseEntity<List<ProjectResponse>> {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject

        return ResponseEntity.ok(projectService.searchRecommendedProjects(userId))
    }

    @GetMapping("/getWhoLiked")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Показать аккаунты, которые лайкали проект")
    fun showWhoLikedProjects(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @RequestParam idProject: String
    ): ResponseEntity<List<UserResponse>> {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject

        return ResponseEntity.ok(projectService.getAllAccountWhoLikedProject(userId, idProject))
    }

    @GetMapping("/getLiked")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Показать лайкнутые пользователем проекты")
    fun showLikedProjects(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
    ): ResponseEntity<List<ProjectResponse>> {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject

        return ResponseEntity.ok(projectService.getAllLikedProjects(userId))
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

    @PostMapping("/addTag")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Добавление в проект тегов")
    fun addTagsToProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @RequestBody tags: List<TagRequest>,
        @RequestParam idProject: String
    ): ProjectResponse {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return projectService.addTagListToProject(tags, idProject, userId)
    }

    @PutMapping("/updateProject")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Обновить проект по id")
    fun updateProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @RequestBody projectRequest: ProjectRequest,
        @RequestParam idProject: String
    ): ProjectResponse {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return projectService.updateProject(projectRequest, userId, idProject)
    }

    @PostMapping("/search")
    @Operation(summary = "Поиск проекта по вводимому текствому значению")
    fun searchProject(
        @RequestParam key: String
    ): ResponseEntity<List<ProjectResponse>> {
        return ResponseEntity.ok(projectService.searchProject(key))
    }

    @DeleteMapping("/removeLike/{idProject}")
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

    @PostMapping("/view/{idProject}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Оставь в истории просмотр на проекте")
    fun viewHistoryProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @PathVariable idProject: String
    ): ProjectResponse {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject

        return projectService.viewProject(idProject, userId)
    }

    @GetMapping("/showViews")
    @Operation(summary = "Показать количество просмотров проекта")
    fun getViewsCount(
        @RequestParam idProject: String
    ): ResponseEntity<String> {

        val res: Int
        try {
            res = projectService.getProjectViewsCount(idProject)
        } catch (exception: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.message);
        }

        var json = JsonObject()
        json.addProperty("int", res)

        return ResponseEntity.status(HttpStatus.OK)
            .body(json.toString());
    }


    @GetMapping("/showLikes")
    @Operation(summary = "Показать количество лайков проекта")
    fun getLikesCount(
        @RequestParam idProject: String
    ): ResponseEntity<String> {

        val res: Int
        try {
            res = projectService.getProjectLikesCount(idProject)
        } catch (exception: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.message);
        }

        var json = JsonObject()
        json.addProperty("int", res)
        return ResponseEntity.status(HttpStatus.OK)
            .body(json.toString());
    }

    @GetMapping("/sortBy")
    @Operation(summary = "Сортировка проекта по столбцу")
    fun sortProjectsBy(
        @RequestParam(defaultValue = "0") pageNo: Int,
        @RequestParam(defaultValue = "10") pageSize: Int,
        @RequestParam(defaultValue = "id") sortBy: String
    ): ResponseEntity<List<ProjectResponse>> {
        return ResponseEntity.ok(projectService.getSortedByNameProjects(pageNo, pageSize, sortBy))
    }

    @GetMapping("/sortByLikes")
    @Operation(summary = "Сортировка проекта по количеству лайков")
    fun sortProjectsByLikes(): ResponseEntity<List<ProjectResponse>> {
        return ResponseEntity.ok(projectService.getSortedByLikes())
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    fun deleteProject(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @RequestBody projectRemoval: ProjectRemoval
    ) {
        val userId =
            (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return projectService.deleteProjectById(projectRemoval.projectId, userId)
    }
}