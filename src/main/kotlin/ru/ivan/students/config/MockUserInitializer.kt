package ru.ivan.students.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import ru.ivan.students.dto.request.RegistrationRequest
import ru.ivan.students.service.KeycloakService
import ru.ivan.students.service.StepikService

/**
 * Класс для очистки пользователей и создания двух новых
 * Нужен для синхронизации keycloak с таблицей accounts
 */
@Profile("initMockUsers")
@Component
class MockUserInitializer : CommandLineRunner {
    @Autowired
    private lateinit var keycloakService: KeycloakService

    @Autowired
    private lateinit var stepikService: StepikService

    override fun run(vararg args: String?) {
        keycloakService.removeAllUsers()
        println("all users are removed")
        createMockUsers()
        println("user1-user1 and user2-user2 are created")
        stepikService.getCourses()
        println("courses from Stepik loaded")
    }

    private fun createMockUsers() {
        val user1: RegistrationRequest = RegistrationRequest(
            email = "user1",
            nickname = "user1",
            phoneNumber = "user1",
            firstName = "user1",
            lastName = "user1",
            telegram = "user1",
            password = "user1"
        )
        val user2: RegistrationRequest = RegistrationRequest(
            email = "user2",
            nickname = "user2",
            phoneNumber = "user2",
            firstName = "user2",
            lastName = "user2",
            telegram = "user2",
            password = "user2"
        )
        val user3: RegistrationRequest = RegistrationRequest(
            email = "user3",
            nickname = "user3",
            phoneNumber = "user3",
            firstName = "user3",
            lastName = "user3",
            telegram = "user3",
            password = "user3"
        )
        val user4: RegistrationRequest = RegistrationRequest(
            email = "user4",
            nickname = "user4",
            phoneNumber = "user4",
            firstName = "user4",
            lastName = "user4",
            telegram = "user4",
            password = "user4"
        )
        keycloakService.registerUser(user1)
        keycloakService.registerUser(user2)
        keycloakService.registerUser(user3)
        keycloakService.registerUser(user4)
    }
}