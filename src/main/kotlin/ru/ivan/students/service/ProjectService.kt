package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Account
import ru.ivan.students.domian.Project
import ru.ivan.students.domian.toResponse
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.dto.response.ProjectResponse
import ru.ivan.students.repository.AccountRepository
import ru.ivan.students.repository.ProjectRepository
import ru.ivan.students.repository.TagRepository
import java.util.*

@Service
class ProjectService {
    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    fun addProject(project: ProjectRequest, userId: String): ProjectResponse {
        return projectRepository.save(
            project.toEntity(userId)
        ).toResponse()
    }

    fun updateProject(project: ProjectRequest, userId: String, projectId: String): ProjectResponse {
        // https://stackoverflow.com/questions/11881479/how-do-i-update-an-entity-using-spring-data-jpa
        // Здесь хитрая штука: метод save смотрит на id сущности, если его нет, то он создает сущность,
        // а если он есть, то апдейтит сущность
        return projectRepository.save(
            project.toEntity(userId, projectId)
        ).toResponse()
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

    fun deleteLikeProject(idProject: String, userId: String): ProjectResponse {
        var project = projectRepository.getById(idProject)
        var account: Account = accountRepository.getById(userId)

        if (account.likes.contains(project))
            account.likes.remove(project)

        projectRepository.save(project)
        accountRepository.save(account)
        return project.toResponse()
    }

    fun likeProject(idProject: String, userId: String): ProjectResponse {
        var project = projectRepository.getById(idProject)
        var account: Account = accountRepository.getById(userId)


        var createdProjects = projectRepository.findByCreatorId(userId)

        if (createdProjects.contains(project))
            throw RuntimeException("User $userId can't like his created projected $idProject")

        account.likes.add(project)
        project.accounts.add(account)
        projectRepository.save(project)
        accountRepository.save(account)
        return project.toResponse()
    }


    fun getAllLikedProjects(accountId: String): List<ProjectResponse> {
        var account = accountRepository.getById(accountId)
        var likedProjects = account.likes;

        var res = mutableListOf<ProjectResponse>()
        for (it in likedProjects) {
            res.add(it.toResponse())
        }

        return res
    }

    fun getAllUserProjects(accountId: String): List<ProjectResponse> {
        var projects = projectRepository.findByCreatorId(accountId)

        var res = mutableListOf<ProjectResponse>()
        for (it in projects) {
            res.add(it.toResponse())
        }

        return res
    }

    /***
     * Logic of recommendations
     */
    fun searchRecommendedProjects(accountId: String): List<ProjectResponse> {
        var account = accountRepository.findById(accountId).orElseThrow {
            RuntimeException("Account with id $accountId does not exist")
        }

        var tags = account.likes!!.flatMap { it.tags }.map { it.name }.distinct()

        // Get all another project which are not the same
        var allProjects = projectRepository.findAll()
        allProjects.removeAll { account.likes!!.contains(it) }


        var recommends = mutableListOf<ProjectResponse>()
        for (it in allProjects) {
            for (el in it.tags)
                if (tags.contains(el.name)) {
                    recommends.add(it.toResponse())
                }
        }
        return recommends
    }

    //    fun showAll(): List<ProjectResponse> {
//        var allProjects = projectRepository.findAll()
//        var res = mutableListOf<ProjectResponse>()
//        for (it in allProjects) {
//            res.add(it.toResponse())
//        }
//        return res
//        return projectRepository.findAll()
//                .map {
//                        project -> project.toResponse()
//                }
//    }

    fun showAll() = projectRepository.findAll()
        .map { project ->
            project.toResponse()
        }


    fun getProjectLikesCount(idProject: String): Int {
        // getById не обращается к базе, а создает прокси объект,
        // то есть пустой объект, у которого заполнено только id, которое ты передал
        // метод findById обращается к базе
        // todo: оптимизировать для хибернейта, не выгружать все аккаунты, если нам нужно лишь их кол-во
        return projectRepository.findById(idProject).orElseThrow {
            RuntimeException("getProjectLikesCount failed. No project found with id $idProject")
        }.accounts.count()
    }

    fun getProjectViewsCount(idProject: String): Int {
        return projectRepository.findById(idProject).orElseThrow {
            RuntimeException("getProjectViewsCount failed. No project found with id $idProject")
        }.accountsView.count()
    }


    fun viewProject(idProject: String, userId: String): ProjectResponse {
        var project = projectRepository.getById(idProject)
        var account: Account = accountRepository.getById(userId)

        account.views.add(project)
        project.accountsView.add(account)
        projectRepository.save(project)
        accountRepository.save(account)
        return project.toResponse()
    }

}