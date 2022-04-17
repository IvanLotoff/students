package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.ivan.students.dto.request.RegistrationRequest
import ru.ivan.students.dto.request.UserAuthRequest
import ru.ivan.students.service.KeycloakService
import ru.ivan.students.util.toJson

@RestController
@RequestMapping("/auth")
class KeycloakController {
    @Autowired
    private lateinit var keycloakService: KeycloakService

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя")
    fun register(
        @RequestBody registrationRequest: RegistrationRequest
    ): String? {
        return keycloakService.registerUserAndGetToken(registrationRequest)?.toJson()
    }

    @PostMapping("/authUser")
    @Operation(summary = "Вход пользователя")
    fun auth(
        @RequestBody userAuthRequest: UserAuthRequest
    ): String? {
        return keycloakService.authUser(userAuthRequest)?.toJson()
    }
}