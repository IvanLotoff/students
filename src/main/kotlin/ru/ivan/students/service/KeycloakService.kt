package ru.ivan.students.service

import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Account
import ru.ivan.students.dto.request.RegistrationRequest
import ru.ivan.students.dto.request.UserAuthRequest
import ru.ivan.students.dto.response.UserResponse


@Service
class KeycloakService {
    @Autowired
    private lateinit var keycloak: Keycloak

    @Autowired
    private lateinit var accountService: AccountService

    fun registerUserAndGetToken(registrationRequest: RegistrationRequest): AccessTokenResponse? {
        //Добавление в реляционную БД
        val user = registerUser(registrationRequest)

        return Keycloak.getInstance(
            "http://localhost:8484/auth",
            "test_realm",
            user.username,
            registrationRequest.password,
            "login_app"
        ).tokenManager().accessToken
    }

    fun registerUserAndGetUserId(registrationRequest: RegistrationRequest): String? {
        return registerUser(registrationRequest).id
    }

    private fun registerUser(registrationRequest: RegistrationRequest): UserRepresentation {
        val password = preparePasswordRepresentation(registrationRequest.password)
        val user = prepareUserRepresentation(registrationRequest, password)

        val realmResource: RealmResource = keycloak.realm("test_realm")
        val userResource: UsersResource? = realmResource.users()

        val response = userResource?.create(user)
        val userId = CreatedResponseUtil.getCreatedId(response)

        user.id = userId

        val myUserResource = userResource?.get(userId)
        val toRepresentation = realmResource.roles().get("ROLE_USER").toRepresentation()

        myUserResource?.roles()?.realmLevel()?.add(listOf(toRepresentation))

        // Создаем аккаунт в нашей бд
        // id аккаунта совпадает с id пользователя в кейклоке

        val account = Account(id = userId)
        accountService.createAccount(account)

        return user
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
                userRepresentation: UserRepresentation -> userRepresentation.id
        }
        ids?.forEach {
            userResource.get(it)?.remove()
        }

    }

    private fun prepareUserRepresentation(
        request: RegistrationRequest,
        cR: CredentialRepresentation
    ): UserRepresentation {
        val newUser = UserRepresentation()
        newUser.username = request.nickname
        newUser.firstName = request.firstName
        newUser.lastName = request.lastName
        newUser.credentials = listOf(cR)
        newUser.email = request.email
        newUser.isEnabled = true
        newUser.realmRoles = listOf("ROLE_USER")
        val attributes = mapOf(
            TELEGRAM_ATTR to listOf(request.telegram),
            PHONE_NUMBER_ATTR to listOf(request.phoneNumber)
        )
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

   fun getUserInfoById(userId: String): UserResponse = keycloak.realm("test_realm")
       .users()
       .get(userId)
       .toRepresentation()
       .toUserResponse()
}

fun UserRepresentation.toUserResponse(): UserResponse {
    return UserResponse(
        id = this.id,
        email = this.email,
        nickname = this.username,
        phoneNumber = this.attributes[PHONE_NUMBER_ATTR]?.get(0),
        firstName = this.firstName,
        lastName = this.lastName,
        telegram = this.attributes[TELEGRAM_ATTR]?.get(0),
    )
}

const val TELEGRAM_ATTR = "telegram"
const val PHONE_NUMBER_ATTR = "phoneNumber"