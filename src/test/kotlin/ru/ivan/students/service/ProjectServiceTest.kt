package ru.ivan.students.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import ru.ivan.students.domian.Account
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.TagRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.repository.ProjectRepository
import kotlin.test.assertTrue


@SpringBootTest
internal class ProjectServiceTest {

    @Autowired
    private lateinit var projectService: ProjectService

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var projectRepository: ProjectRepository

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
        description = "Написание клиентской части приложения для умной камеры с использованием уже написанного REST API",
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

    private final var account: Account = Account(id = "012424")
    private final var accountAnother: Account = Account(id = "555")
    var project = pr1.toEntity(account.id.toString())

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
        accountService.deleteAllAccounts()
        projectService.deleteAllProjects()
    }

    /**
     * Check search account by id
     */
    @Test
    fun findAccountByIdTest() {
        val str = account.id.toString()
        accountService.createAccount(account)
        val res = accountService.findAccountById(str).get()
        assertEquals(account.id, res.id)
    }

    /**
     * Check create account
     */
    @Test
    fun createAccountTest() {
        val acc2 = accountService.createAccount(account)
        assertEquals(acc2, account)
    }

    @Test
    @Transactional
    fun createProjectTest() {
        val str = account.id.toString()
        accountService.createAccount(account)
        account = accountService.findAccountById(str).get()

        val tempProject =
            projectRepository.findByIdAndDeletionDateNull(projectService.addProject(pr1, account.id.toString()).id)
                .orElseThrow()
        assertEquals(tempProject.title, project.title)
        assertEquals(tempProject.description, project.description)
        assertEquals(tempProject.creatorId, project.creatorId)
        assertEquals(tempProject.communication, project.communication)
        assertEquals(tempProject.tags.map { it.name }, project.tags.map { it.name })
    }

    @Test
    @Transactional
    fun searchProject() {
        val str = account.id.toString()
        accountService.createAccount(account)
        account = accountService.findAccountById(str).get()

        val tempProject =
            projectRepository.findByIdAndDeletionDateNull(projectService.addProject(pr1, account.id.toString()).id)
                .orElseThrow()

        val searchRes = projectService.searchProject("Android ")[0]

        assertEquals(tempProject.title, searchRes.title)
    }

    @Test
    @Transactional
    fun recommendProject() {
        val id1 = account.id.toString()
        accountService.createAccount(account)
        account = accountService.findAccountById(id1).get()

        val id2 = accountAnother.id.toString()
        accountService.createAccount(accountAnother)

        val tempProject1 =
            projectRepository.findByIdAndDeletionDateNull(projectService.addProject(pr1, account.id.toString()).id)
                .orElseThrow()

        val tempProject2 =
            projectRepository.findByIdAndDeletionDateNull(projectService.addProject(pr2, account.id.toString()).id)
                .orElseThrow()

        val tempProject3 =
            projectRepository.findByIdAndDeletionDateNull(projectService.addProject(pr3, account.id.toString()).id)
                .orElseThrow()

        projectService.likeProject(tempProject3.id.toString(), id2)
        val searchRes = projectService.searchRecommendedProjects(id2)
        var titleArray = searchRes.map { it.title }

        assertTrue { titleArray.contains(tempProject1.title) && titleArray.contains(tempProject2.title) }
    }
}