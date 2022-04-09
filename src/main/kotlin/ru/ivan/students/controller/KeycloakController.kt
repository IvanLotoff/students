package ru.ivan.students.controller

import org.keycloak.representations.AccessTokenResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ivan.students.dto.request.RegistrationRequest
import ru.ivan.students.service.KeycloakService
import ru.ivan.students.util.toJson
import javax.ws.rs.core.Response

@RestController
@RequestMapping("/auth")
class KeycloakController {
    @Autowired
    private lateinit var keycloakService: KeycloakService

    @PostMapping("/register")
    fun register(
        @RequestBody registrationRequest: RegistrationRequest
    ): String? {
        return keycloakService.registerUser(registrationRequest)?.toJson()
    }
}