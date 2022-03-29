package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.domian.CV
import ru.ivan.students.dto.CVDto
import ru.ivan.students.service.CVService
import java.security.Principal

@RestController
@RequestMapping("/api/cv")
class CVController {
    @Autowired
    private lateinit var cvService: CVService

    @GetMapping("/all")
    fun showAll(): List<CV> = cvService.showAll()

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "security_auth")
    fun addCV(principal: Principal, @RequestBody cvDto: CVDto): ResponseEntity<CV> {
        return ResponseEntity.ok(cvService.saveCV(cvDto))
    }

}