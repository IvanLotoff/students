package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Account
import ru.ivan.students.domian.toResponse
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.dto.response.ProjectResponse
import ru.ivan.students.mapper.ProjectConverter
import ru.ivan.students.repository.AccountRepository
import ru.ivan.students.repository.ProjectRepository
import ru.ivan.students.repository.TagRepository

@Service
class ProjectService {
    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var tagRepository: TagRepository

    //TODO: Трабл с сохранением тэг два раза, один из тег идентичный, но без ссылки на проект
    fun addProject(project: ProjectRequest, userId: String): ProjectResponse {
        val converter = ProjectConverter()
        var pr = project.toEntity(userId)
        pr.creatorId = userId
        var save = projectRepository.save(pr)

        var tags = project.tags

        converter.toListOfTagToRequest(tags).forEach { value ->
            value.project = pr
            println("${value.name}  ${value.project}")
            tagRepository.save(value)
        }

        if (tags.isEmpty()) {
            return save.toResponse()
        }

        pr.tags = converter.toListOfTagToRequest(tags)
        save = projectRepository.save(pr)
        return save.toResponse()
    }

    //TODO: ДОписать после фикса Вани взаимодействие с тегами,:  Если список не пустой, то заменяем им старый
    fun updateProject(project: ProjectRequest, userId: String, projectId: String): ProjectResponse {
        var oldProject = projectRepository.getById(projectId)

        if (!oldProject.creatorId.equals(userId))
            return oldProject.toResponse()

        var pr = project.toEntity(userId)

        oldProject.tags = pr.tags
        oldProject.title = pr.title
        oldProject.description = pr.description
        oldProject.communication = pr.communication


        return projectRepository.save(oldProject).toResponse()
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

        // TODO: Что возвращать если не проходит проверку в сервисе, экспешн?
        if (createdProjects.contains(project))
            return project.toResponse()

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
        var account = accountRepository.findById(accountId).get()

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

    fun showAll(): List<ProjectResponse> {
        var allProjects = projectRepository.findAll()
        var res = mutableListOf<ProjectResponse>()
        for (it in allProjects) {
            res.add(it.toResponse())
        }
        return res
    }

    fun getProjectLikesCount(idProject: String): Int {
        var project = projectRepository.getById(idProject)
        return project.accounts.count()
    }

    fun getProjectViewsCount(idProject: String): Int {
        var project = projectRepository.getById(idProject)
        return project.accountsView.count()
    }


    fun viewProject(idProject: String, userId: String): ProjectResponse {
        var project = projectRepository.getById(idProject)
        var account: Account = accountRepository.getById(userId)


        var createdProjects = projectRepository.findByCreatorId(userId)

        // TODO: Что возвращать если не проходит проверку в сервисе, экспешн?
        if (createdProjects.contains(project))
            return project.toResponse()

        account.views.add(project)
        project.accountsView.add(account)
        projectRepository.save(project)
        accountRepository.save(account)
        return project.toResponse()
    }

}