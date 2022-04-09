package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.domian.Account
import ru.ivan.students.dto.request.AccountRequest
import ru.ivan.students.mapper.AccountDTOToAccount
import ru.ivan.students.service.AccountService
import ru.ivan.students.util.toJson
import java.security.Principal

@RestController
@RequestMapping("/api/account")
class UserController {

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "apiKey")
    @Operation(summary = "Извлекаем информацию из JWT токена")
    fun extractJWTToken(keycloakAuthenticationToken: KeycloakAuthenticationToken): String {
        return (keycloakAuthenticationToken.principal as KeycloakPrincipal<*>).keycloakSecurityContext.token.toJson()
    }
}