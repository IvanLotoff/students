package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/test/")
class ProjectController {

        @GetMapping("user")
        @PreAuthorize("hasRole('USER')")
        @SecurityRequirement(name = "security_auth")
        fun getUserInfo(principal: Principal): ResponseEntity<String> {
            val keycloakAuthenticationToken = principal as KeycloakAuthenticationToken
            val accessToken = keycloakAuthenticationToken.account.keycloakSecurityContext.token
            return ResponseEntity.ok("User info")
        }

}