package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.domian.Account
import ru.ivan.students.domian.Project
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.mapper.AccountDTOToAccount
import ru.ivan.students.service.AccountService
import ru.ivan.students.service.ProjectService
import java.security.Principal

@RestController
@RequestMapping("/api/project/")
class ProjectController {

    @Autowired
    private lateinit var projectService: ProjectService

    @GetMapping("user")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "security_auth")
    fun getUserInfo(principal: Principal): ResponseEntity<String> {
        val keycloakAuthenticationToken = principal as KeycloakAuthenticationToken
        val accessToken = keycloakAuthenticationToken.account.keycloakSecurityContext.token
        return ResponseEntity.ok("User info")
    }

    @PostMapping("user")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "security_auth")
    fun showRecommendedProjects(principal: Principal,@RequestBody projectRequest: ProjectRequest): List<ResponseEntity<Project>> {
        //Добавить по принципал акк пользователя
        principal.name
        return projectService.searchRecommendedProjects(Account()).map {
            ResponseEntity.ok(it)
        }
    }

}