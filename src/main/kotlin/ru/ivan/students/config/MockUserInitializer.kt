package ru.ivan.students.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import ru.ivan.students.service.AccountService
import ru.ivan.students.service.KeycloakService
import kotlin.math.log

/**
 * Класс для очистки пользователей и создания двух новых
 * Нужен для синхронизации keycloak с таблицей accounts
 */
@Profile("initMockUsers")
@Component
class MockUserInitializer: CommandLineRunner {
    @Autowired
    private lateinit var keycloakService: KeycloakService

    override fun run(vararg args: String?) {
        keycloakService.removeAllUsers()
        println("all users are removed")
        keycloakService.createTwoMockUsers()
        println("user1-user1 and user2-user2 are created")
    }
}