package ru.ivan.students.controller

import org.json.JSONObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.ivan.students.domian.Tag
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.RegistrationRequest
import ru.ivan.students.service.KeycloakService
import ru.ivan.students.service.ProjectService
import ru.ivan.students.util.getJsonField
import ru.ivan.students.util.getJsonFieldFromArrayWrapped
import kotlin.test.assertTrue


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebMvcTest(KeycloakController::class)
@AutoConfigureMockMvc
internal class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var keycloakService: KeycloakService

    @Autowired
    private lateinit var projectService: ProjectService
    private lateinit var userIdsByUsername: Map<String, String>

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
            .contentType(APPLICATION_JSON)
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
            .accept(APPLICATION_JSON)

        val body = mockMvc.perform(req)
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andReturn()
            .response
            .contentAsString

        println(body)
        assertTrue { body.contains("access_token") }
    }

    @Test
    fun projectDeletionTest() {
        // Авторизируемся от пользователя user1: user1
        val accessTokenUser1 = authUser(
            username = "user1",
            password = "user1"
        )

        // Получаем id проекта, который создан user1
        val userProjectsRequest: MockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/api/project/getCreated")
            .header("Authorization", "Bearer $accessTokenUser1")
            .contentType(APPLICATION_JSON)

        val createdProjectsBody = mockMvc.perform(userProjectsRequest)
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andReturn()
            .response
            .contentAsString
        println("createdProjectsBody $createdProjectsBody")
        val projectId = createdProjectsBody.getJsonFieldFromArrayWrapped("id")

        // Удаляем проект с projectId
        deleteProjectById(accessTokenUser1, projectId)

        // Ещё раз получаем id проекта, который создан user1
        // Теперь мы ожидаем пустой массив, так как проект был удален
        val userProjectsNewRequest: MockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/api/project/getCreated")
            .header("Authorization", "Bearer $accessTokenUser1")
            .contentType(APPLICATION_JSON)

        val createdProjectsBodyNew = mockMvc.perform(userProjectsNewRequest)
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andExpect(content().string("[]"))
            .andReturn()
            .response
            .contentAsString

    }

    @Test
    fun getWhoLikedTest() {
        // Авторизируемся от пользователя user1: user1
        val accessTokenUser1 = authUser(
            username = "user1",
            password = "user1"
        )

        // Авторизируемся от пользователя user2: user2
        val accessTokenUser2 = authUser(
            username = "user2",
            password = "user2"
        )

        // Создаем новый проект от первого пользователя
        val newProjectFromUser1Id = addProject(
            accessToken = accessTokenUser1,
            title = "titleUser1",
            description = "descriptionUser1",
            communication = "communication"
        )

        // Лайкаем проект первого пользователя от имени второго пользователя
        likeProject(accessTokenUser2, newProjectFromUser1Id)

        // Проверяем, найдется ли id второго в json ответе метода getWhoLiked с
        // access_token-ом первого пользователя
        assertUserWithIdLikedProject(
            accessToken = accessTokenUser1,
            idProject = newProjectFromUser1Id,
            userId = userIdsByUsername["user1"]
        )
    }

    private fun assertUserWithIdLikedProject(
        accessToken: Any?,
        idProject: Any?,
        userId: Any?
    ): Boolean {
        val req: MockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/api/project/getWhoLiked?idProject=$idProject")
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer $accessToken")

        val body = mockMvc.perform(req)
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andReturn()
            .response
            .contentAsString

        return body.contains(userId.toString())
    }

    private fun likeProject(
        accessToken: Any?,
        idProject: Any?
    ) {
        val req: MockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/project/like/$idProject")
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer $accessToken")

        mockMvc.perform(req)
    }

    private fun addProject(
        accessToken: Any?,
        title: String? = null,
        description: String? = null,
        communication: String? = null,
        tag: Tag? = null
    ): Any? {
        val req: MockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/project/add")
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer $accessToken")
            .content(
                JSONObject(
                    """{
                          "title": "$title",
                          "description": "$description",
                          "communication": "$communication",
                          "tags": [
                            {
                              "name": "${tag?.name}",
                              "about": "${tag?.about}"
                            }
                          ]
                        }"""
                ).toString()
            )
            .accept(APPLICATION_JSON)

        val body = mockMvc.perform(req)
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andReturn()
            .response
            .contentAsString

        val projectId = body.getJsonField("id")
        println("projectId = $projectId")
        return projectId
    }

    private fun deleteProjectById(accessToken: Any?, projectId: Any?) {
        val deleteProjectRequest: MockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/api/project/delete")
            .header("Authorization", "Bearer $accessToken")
            .content(
                JSONObject(
                    """{
                            "projectId": $projectId
                        }"""
                ).toString()
            )
            .contentType(APPLICATION_JSON)

        // Удаляем проект по id
        mockMvc.perform(deleteProjectRequest)
            .andExpect(status().isOk)
            .andReturn()
    }

    private fun authUser(username: String, password: String): Any? {
        val loginRequest: MockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/auth/authUser")
            .contentType(APPLICATION_JSON)
            .content(
                JSONObject(
                    """{
                          "username": "$username",
                          "password": "$password"
                    }"""
                ).toString()
            )
            .accept(APPLICATION_JSON)

        val loginBody = mockMvc.perform(loginRequest)
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
            .andReturn()
            .response
            .contentAsString

        val accessToken = loginBody.getJsonField("access_token")
        println("access token $accessToken ")
        return accessToken
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

        userIdsByUsername = mapOf(
            "user1" to keycloakService.registerUserAndGetUserId(user1)!!,
            "user2" to keycloakService.registerUserAndGetUserId(user2)!!,
            "user3" to keycloakService.registerUserAndGetUserId(user3)!!,
            "user4" to keycloakService.registerUserAndGetUserId(user4)!!
        )

        createMockProjects(userIdsByUsername.values)
    }

    private fun createMockProjects(ids: Collection<String>) {
        val projectId = mutableListOf<String>()

        for (idUser in ids) {
            val pr = ProjectRequest(
                title = "string",
                description = "string",
                communication = "string",
                tags = mutableListOf()
            )
            val response = projectService.addProject(pr, idUser)
            projectId.add(response.id)
        }
    }

//    @Test
//    fun testUser() {
//        keycloakController = KeycloakController()
//        var request: RegistrationRequest =
//            RegistrationRequest("string12", "string13", "string14", "string15", "string16", "string17", "string18")
//        assertEquals("", keycloakController!!.register(request))
//    }
}