package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Account
import ru.ivan.students.domian.toResponse
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.dto.response.ProjectResponse
import ru.ivan.students.repository.AccountRepository
import ru.ivan.students.repository.ProjectRepository

@Service
class ProjectService {
    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    fun addProject(project: ProjectRequest, userId: String): ProjectResponse {
        return projectRepository.save(project.toEntity(userId)).toResponse()
    }


    /***
     * Logic of search
     */
    fun searchProject(key: String): List<ProjectResponse> {
        var allProjects = projectRepository.findAll()

        var res = mutableListOf<ProjectResponse>()
        for (it in allProjects) {

            if (it.description.contains(key)
                || it.title.contains(key)
                || it.tags.map { it.name }.distinct().contains(key)
                || it.tags.map { it.about }.distinct().contains(key)
            ) {
                res.add(it.toResponse())
            }
        }

        return res
    }

    //ToDO: Автоматом дает ввод с "" в свагере, но с ними не парсит
    // ToDO: меняет лайкнувшего а не создает дубликат
    fun likeProject(idProject: String, userId: String): ProjectResponse {
        println("\n$idProject $userId !!!!\n")

        var project = projectRepository.getById(idProject)
        var account: Account = accountRepository.getById(userId)

        println("\n!!!!!!!!!!!\n$project")

        account.likes.add(project)
        project.accounts.add(account)
        projectRepository.save(project)
        accountRepository.save(account)
        return project.toResponse()
    }


    fun getAllUserProjects(accountId: String): List<ProjectResponse> {
        var projects = projectRepository.findByCreatorId(accountId)

        var recommends = mutableListOf<ProjectResponse>()
        for (it in projects) {
            recommends.add(it.toResponse())
        }

        return recommends
    }

    /***
     * Logic of recommendations
     */
    fun searchRecommendedProjects(accountId: String): List<ProjectResponse> {
        var account = accountRepository.findById(accountId).get()

        var tags = account.likes!!.flatMap { it.tags }.map { it.name }.distinct()

        // Get all another project which are not the same
        var allProjects = projectRepository.findAll()
        println("AllProjects\n")
        println(allProjects)
        allProjects.removeAll { account.likes!!.contains(it) }

        println("AllProjects\n")
        println(allProjects)

        var recommends = mutableListOf<ProjectResponse>()
        for (it in allProjects) {
            for (el in it.tags)
                if (tags.contains(el.name)) {
                    recommends.add(it.toResponse())
                }
        }

        println("All reccomends\n")
        println(recommends)
        return recommends
    }

    fun showAll(): List<ProjectResponse> {
        var allProjects = projectRepository.findAll()
        var res = mutableListOf<ProjectResponse>()
        for (it in allProjects) {
            res.add(it.toResponse())
        }
        return res
    }
}