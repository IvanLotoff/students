package ru.ivan.students.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.RegistrationRequest
import ru.ivan.students.dto.request.TagRequest
import ru.ivan.students.service.KeycloakService
import ru.ivan.students.service.NewsService
import ru.ivan.students.service.ProjectService
import ru.ivan.students.service.StepikService


@Profile("demoProfile")
@Component
class MockUserInitializer : CommandLineRunner {
    @Autowired
    private lateinit var keycloakService: KeycloakService

    @Autowired
    private lateinit var stepikService: StepikService

    @Autowired
    private lateinit var projectService: ProjectService

    @Autowired
    var newsService = NewsService();

    override fun run(vararg args: String?) {
        keycloakService.removeAllUsers()
        println("all users are removed")
        createMockProjects(createMockUsers())
        println("users created")
        stepikService.getCourses(10)
        println("courses from Stepik loaded")
        newsService.getCourses(1)
        println("News loaded")
    }

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
    val user5: RegistrationRequest = RegistrationRequest(
        email = "user5",
        nickname = "string",
        phoneNumber = "user5",
        firstName = "user5",
        lastName = "user5",
        telegram = "user5",
        password = "string"
    )

    private fun createMockUsers(): List<String> {
        val listId = mutableListOf<String>()
        listId.add(keycloakService.registerUserAndGetUserId(user1)!!)
        listId.add(keycloakService.registerUserAndGetUserId(user2)!!)
        listId.add(keycloakService.registerUserAndGetUserId(user3)!!)
        listId.add(keycloakService.registerUserAndGetUserId(user4)!!)
        listId.add(keycloakService.registerUserAndGetUserId(user5)!!)
        return listId
    }

    private final val tagList = mutableListOf(
        TagRequest(
            name = "Kotlin",
            about = "Kotlin language junior skills",
        ), TagRequest(
            name = "Android",
            about = "Android studio experience",
        )
    )

    private final val pr1 = ProjectRequest(
        title = "Android app smart camera",
        description = "Написание клиентской части приложения для умной камеры с использованием созданного REST API",
        communication = "vk: smart_camera",
        tags = tagList
    )

    private final val pr2 = ProjectRequest(
        title = "Android project",
        description = "Проект по разработке приложения погоды на Android c использованием языка Kotlin",
        communication = "telegram: @androider",
        tags = tagList
    )

    private final val pr3 = ProjectRequest(
        title = "Calculator app",
        description = "Kotlin calculator project course work",
        communication = "email : calc@mailrambler.ru",
        tags = mutableListOf<TagRequest>()
    )

    private final val pr4 = ProjectRequest(
        title = "3d game",
        description = "Game on Unity 3D c# project CourseWork",
        communication = "email : game@mailrambler.ru",
        tags = mutableListOf<TagRequest>()
    )

    private final val pr5 = ProjectRequest(
        title = "Mathematics research",
        description = "Algebra and math analysis",
        communication = "email : akge@gmalire.com",
        tags = mutableListOf<TagRequest>()
    )

    private fun createMockProjects(ids: List<String>) {
        val projectId = mutableListOf<String>()

        val projectList = mutableListOf<ProjectRequest>(pr1, pr2, pr3, pr4, pr5)

        var i = 0
        for (idUser in ids) {
            var response = projectService.addProject(projectList[i], idUser)
            projectId.add(response.id)
//            projectService.likeProject(response.id, idUser)
//            projectService.viewProject(response.id, idUser)
            i++
        }

        i = 0
        for (idUser in ids) {
            i++
            projectService.likeProject(projectId[i % 3], idUser)
            projectService.viewProject(projectId[i % 3], idUser)
        }

        projectService.viewProject(projectId[3], ids[0])
        projectService.likeProject(projectId[3], ids[0])

        projectService.deleteLikeProject(projectId[0], ids[0])
        projectService.deleteLikeProject(projectId[3], ids[0])

        projectService.updateProject(projectList[0], ids[0], projectId[0])
        //projectService.addTagListToProject(tagList, projectId[0], ids[0])

        projectService.searchRecommendedProjects(ids[0])

        projectService.getSortedByLikes()
    }

    //    private fun registerAndGetID(user: RegistrationRequest): String {
//        val chunks: List<String> = keycloakService.registerUserAndGetToken(user)?.token!!.split(".")
//        val decoder: Base64.Decoder = Base64.getUrlDecoder()
//        val payload = String(decoder.decode(chunks[1]))
//        return ObjectMapper().readValue(payload, ObjectNode::class.java).get("sub").toString().replace("\"", "")
//    }

}