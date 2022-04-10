package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.dto.request.CVRequest
import ru.ivan.students.dto.response.CVResponse
import ru.ivan.students.service.CVService

@RestController
@RequestMapping("/api/cv")
class CVController {
    @Autowired
    private lateinit var cvService: CVService

    @GetMapping("/all")
    fun showAll(): List<CVResponse> = cvService.showAll()

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    fun addCV(
        keycloakAuthenticationToken: KeycloakAuthenticationToken,
        @RequestBody CVRequest: CVRequest
    ): ResponseEntity<CVResponse> {
        val userId =  (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.subject
        return ResponseEntity.ok(cvService.saveCV(CVRequest, userId))
    }
}