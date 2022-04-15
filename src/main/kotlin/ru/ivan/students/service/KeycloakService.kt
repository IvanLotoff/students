package ru.ivan.students.service

import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Account
import ru.ivan.students.dto.request.RegistrationRequest
import ru.ivan.students.dto.request.UserAuthRequest


@Service
class KeycloakService {
    @Autowired
    private lateinit var keycloak: Keycloak
    @Autowired
    private lateinit var accountService: AccountService

    fun registerUser(registrationRequest: RegistrationRequest): AccessTokenResponse? {
        val password = preparePasswordRepresentation(registrationRequest.password)
        val user = prepareUserRepresentation(registrationRequest, password)

        val realmResource: RealmResource = keycloak.realm("test_realm")
        val userResource: UsersResource? = realmResource.users()

        val response = userResource?.create(user)
        val userId = CreatedResponseUtil.getCreatedId(response)

        val myUserResource = userResource?.get(userId)
        val toRepresentation = realmResource.roles().get("ROLE_USER").toRepresentation()

        myUserResource?.roles()?.realmLevel()?.add(listOf(toRepresentation))

        // Создаем аккаунт в нашей бд
        // id аккаунта совпадает с id пользователя в кейклоке
        val account = Account(id = userId)
        accountService.createAccount(account)

        return Keycloak.getInstance(
            "http://localhost:8484/auth",
            "test_realm",
            user.username,
            registrationRequest.password,
            "login_app"
        ).tokenManager().accessToken
    }

    fun authUser(userAuthRequest: UserAuthRequest): AccessTokenResponse? {
        return Keycloak.getInstance(
            "http://localhost:8484/auth",
            "test_realm",
            userAuthRequest.username,
            userAuthRequest.password,
            "login_app"
        ).tokenManager().accessToken
    }

    fun removeAllUsers() {
        val userResource: UsersResource? = keycloak.realm("test_realm").users()
        val ids = userResource?.list()?.map {
                userRepresentation -> userRepresentation.id
        }
        ids?.forEach {
            userResource.get(it)?.remove()
        }
    }

    private fun prepareUserRepresentation(request: RegistrationRequest,
                                          cR: CredentialRepresentation): UserRepresentation {
        val newUser = UserRepresentation()
        newUser.username = request.nickname
        newUser.firstName = request.firstName
        newUser.credentials = listOf(cR)
        newUser.email = request.email
        newUser.isEnabled = true
        newUser.realmRoles = listOf("ROLE_USER")
        val attributes = mapOf("telegram" to listOf(request.telegram))
        newUser.attributes = attributes
        return newUser
    }

    private fun preparePasswordRepresentation(password: String): CredentialRepresentation {
        val cR = CredentialRepresentation()
        cR.isTemporary = false
        cR.type = CredentialRepresentation.PASSWORD
        cR.value = password
        return cR
    }
}