package ru.ivan.students.controller

import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.ivan.students.dto.request.RegistrationRequest
import ru.ivan.students.service.KeycloakService
import kotlin.test.assertTrue


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebMvcTest(KeycloakController::class)
@AutoConfigureMockMvc
internal class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var keycloakService: KeycloakService

    @BeforeEach
    fun setUp() {
        keycloakService.removeAllUsers()
        createMockUsers()
    }

    @AfterEach
    fun tearDown() {
        keycloakService.removeAllUsers()
    }

    @Test
    fun simpleTest() {
        val req: MockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                JSONObject(
                    """{
                    "email": "string",
                    "nickname": "string",
                    "phoneNumber": "string",
                    "firstName": "string",
                    "lastName": "string",
                    "telegram": "string",
                    "password": "string"
                }"""
                ).toString()
            )
            .accept(MediaType.APPLICATION_JSON)

        val body = mockMvc.perform(req)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn()
            .response
            .contentAsString


        println(body)
        assertTrue { body.contains("access_token") }
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

//    @Test
//    fun testUser() {
//        keycloakController = KeycloakController()
//        var request: RegistrationRequest =
//            RegistrationRequest("string12", "string13", "string14", "string15", "string16", "string17", "string18")
//        assertEquals("", keycloakController!!.register(request))
//    }
}