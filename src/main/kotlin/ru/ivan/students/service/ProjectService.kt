package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.ivan.students.domian.*
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.TagRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.dto.request.toTagEntityList
import ru.ivan.students.dto.response.ProjectResponse
import ru.ivan.students.dto.response.UserResponse
import ru.ivan.students.repository.AccountRepository
import ru.ivan.students.repository.ProjectAccountRepository
import ru.ivan.students.repository.ProjectRepository
import ru.ivan.students.repository.TagRepository
import java.time.LocalDate
import javax.transaction.Transactional


@Service
class ProjectService {
    @Autowired
    private lateinit var projectRepository: ProjectRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var keycloakService: KeycloakService


    @Autowired
    private lateinit var projectAccountRepository: ProjectAccountRepository

    @Transactional
    fun addProject(project: ProjectRequest, userId: String): ProjectResponse {
        return projectRepository.save(
            project.toEntity(userId)
        ).toResponse()
    }

    @Transactional
    fun addTagListToProject(tags: List<TagRequest>, projectId: String, userId: String): ProjectResponse {
        var project = projectRepository.findByIdAndDeletionDateNull(projectId).orElseThrow {
            RuntimeException("No such project $projectId")
        }

        project.tags.addAll(tags.toTagEntityList(project) as MutableList<Tag>)

        projectRepository.save(
            project
        )

        return projectRepository.findByIdAndDeletionDateNull(projectId).orElseThrow {
            RuntimeException("No such project $projectId")
        }.toResponse()
    }

    @Transactional
    fun updateProject(project: ProjectRequest, userId: String, projectId: String): ProjectResponse {
        //Сброс тегов
        val oldProject = projectRepository.findByIdAndDeletionDateNull(projectId).orElseThrow {
            RuntimeException("No such project $projectId")
        }

        if (oldProject.creatorId != userId) {
            throw RuntimeException("User $userId has no rights to change another $projectId")
        }

        if (oldProject.tags.isNotEmpty()) {
            val tags = oldProject.tags.toMutableList()
            tags.forEach { tag ->
                oldProject.tags.remove(tag)
                tag.project = null
            }
        }
        projectRepository.save(oldProject)

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
    fun searchProject(searchValue: String): List<ProjectResponse> {
        val allProjects = projectRepository.findAll()

        val res = mutableListOf<ProjectResponse>()
        val keys = searchValue.split(" ", ",")

        for (key in keys) {
            for (it in allProjects) {

                if (it.description.contains(key)
                    || it.title.contains(key)
                    || it.tags.map { it.name }.distinct().contains(key)
                    || it.tags.map { it.about }.distinct().contains(key)
                ) {
                    res.add(it.toResponse())
                }
            }
        }

        return res
    }

    @Transactional
    fun deleteLikeProject(idProject: String, userId: String): ProjectResponse {
        var project = projectRepository.findByIdAndDeletionDateNull(idProject).orElseThrow {
            RuntimeException("No such project $idProject")
        }
        val account: Account = accountRepository.findById(userId).orElseThrow {
            RuntimeException("No such user $userId")
        }

        var likes = account.likes
        (likes.forEach {
            if (it.project == project && it.account == account) {
                account.likes.remove(it)
                project.accounts.remove(it)
                projectAccountRepository.delete(it)
            }
        })


        projectRepository.save(project)
        accountRepository.save(account)
        return project.toResponse()
    }

    @Transactional
    fun likeProject(idProject: String, userId: String): ProjectResponse {
        var project = projectRepository.findByIdAndDeletionDateNull(idProject).orElseThrow {
            RuntimeException("No such project $idProject")
        }

        val account: Account = accountRepository.findById(userId).orElseThrow {
            RuntimeException("No such user $userId")
        }


        var createdProjects = projectRepository.findByCreatorIdAndDeletionDateNull(userId)

        if (createdProjects.contains(project) && account.likes.firstOrNull { it.project.id == project.id && it.account.id == account.id } != null)
            throw RuntimeException("User $userId can't like his created or liked projected $idProject")


        val projectAccount: ProjectAccount = ProjectAccount(
            project = project,
            account = account
        )


        projectAccountRepository.save(projectAccount)
        account.likes.add(projectAccount)
        project.accounts.add(projectAccount)
        projectRepository.save(project)
        accountRepository.save(account)
        return project.toResponse()
    }

    fun getAllAccountWhoLikedProject(userId: String, idProject: String): List<UserResponse> {
        val project = projectRepository.findById(idProject).orElseThrow {
            RuntimeException("getProjectViewsCount failed. No project found with id $idProject")
        }

        if (project.creatorId != userId) {
            throw RuntimeException("User $userId has no rights to check who liked another $project")
        }

        return project.accounts.map { it -> keycloakService.getuserInfoById(it.account.id!!) }
    }

    fun getAllLikedProjects(accountId: String): List<ProjectResponse> {

        val account = accountRepository.findById(accountId).orElseThrow {
            RuntimeException("No such user $accountId")
        }

        val likedProjects = account.likes

        println(account)
        println(likedProjects)

        val res = mutableListOf<ProjectResponse>()
        for (it in likedProjects) {
            res.add(it.project.toResponse())
        }

        return res
    }

    fun getAllUserProjects(accountId: String): List<ProjectResponse> {
        var projects = projectRepository.findByCreatorIdAndDeletionDateNull(accountId)

        val res = mutableListOf<ProjectResponse>()
        for (it in projects) {
            res.add(it.toResponse())
        }

        return res
    }

    /***
     * Logic of recommendations
     */
    @Transactional
    fun searchRecommendedProjects(accountId: String): List<ProjectResponse> {
        val account = accountRepository.findById(accountId).orElseThrow {
            RuntimeException("Account with id $accountId does not exist")
        }

        val tags: MutableList<String> =
            account.likes.map { it.project }.flatMap { it.tags }.map { it.name }.distinct().toMutableList()
        tags.addAll(account.likes.map { it.project }.flatMap { it.title.split(" ") }.distinct().toMutableList())

        // Get all another project which are not the same
        val allProjects = projectRepository.findAll()
        allProjects.removeAll { account.likes.map { it.project }.contains(it) }


        val recommends = mutableListOf<ProjectResponse>()
        for (it in allProjects) {
            if (it.creatorId != accountId) {
                var flag = false
                for (tag in tags) {
                    if (it.title.contains(tag) || it.description.contains(tag)) {
                        flag = true
                        break
                    }
                }

                for (el in it.tags)
                    if (tags.contains(el.name) || it.description.contains(el.name) || it.title.contains(el.name)) {
                        recommends.add(it.toResponse())
                        flag = true
                        break
                    }

                if (flag) {
                    recommends.add(it.toResponse())
                }
            }
        }

//        println("!!!!!!!!!")
//        for (rec in recommends.distinct())
//            println("$rec \n")

        return recommends.distinct()
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

    fun showAll() = projectRepository.findAllByDeletionDateNull()
        .map { project ->
            project.toResponse()
        }


    fun getProjectLikesCount(idProject: String): Int {
        // getById не обращается к базе, а создает прокси объект,
        // то есть пустой объект, у которого заполнено только id, которое ты передал
        // метод findById обращается к базе
        // todo: оптимизировать для хибернейта, не выгружать все аккаунты, если нам нужно лишь их кол-во
        return projectRepository.findByIdAndDeletionDateNull(idProject).orElseThrow {
            RuntimeException("getProjectLikesCount failed. No project found with id $idProject")
        }.accounts.count()
    }

    fun getProjectViewsCount(idProject: String): Int {
        return projectRepository.findByIdAndDeletionDateNull(idProject).orElseThrow {
            RuntimeException("getProjectViewsCount failed. No project found with id $idProject")
        }.accountsView.count()
    }

    @Transactional
    fun viewProject(idProject: String, userId: String): ProjectResponse {
        val project = projectRepository.findByIdAndDeletionDateNull(idProject).orElseThrow {
            RuntimeException("No such project $idProject")
        }


        val account: Account = accountRepository.findById(userId).orElseThrow {
            RuntimeException("No such user $userId")
        }

        if (project.accountsView.contains(account))
            throw RuntimeException("User $userId already viewed project $idProject")

        account.views.add(project)
        project.accountsView.add(account)
        projectRepository.save(project)
        accountRepository.save(account)
        return project.toResponse()
    }

    fun getSortedByNameProjects(pageNumber: Int, pageSize: Int, sortBy: String): List<ProjectResponse> {
        val pageable: Pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, sortBy))
        val pageRes = projectRepository.findAll(pageable)

        return if (pageRes.hasContent()) {
            pageRes.content.map { pr -> pr.toResponse() }
        } else {
            return ArrayList()
        }
    }

    fun getSortedByLikes(): List<ProjectResponse> {
        val projectIds = projectRepository.orderIdsByLikes()
        val projectsResponse = mutableListOf<ProjectResponse>()
        for (id in projectIds) {
            val project = projectRepository.findById(id).orElseThrow {
                RuntimeException("No such project $id")
            }
            projectsResponse.add(project.toResponse())
        }

        return projectsResponse

        //Аналогичный способ достать сразу коллекцию стрингов-проектов, но тогда нужно кастить
        //return projectRepository.orderByLikes()
        //            .map { it ->
        //                ObjectMapper().readValue(it, Project::class.java)
        //            }
    }

    fun deleteProjectById(projectId: String, userId: String) {
        // Проверяем, что проект создан именно этим пользователем
        val userProject = projectRepository.findByCreatorIdAndDeletionDateNull(userId)
            .firstOrNull { project ->
                println("project.id = ${project.id} , projectId = $projectId , predicate matches ${project.id.toString() == projectId}")
                project.id.toString() == projectId
            }
            ?: throw RuntimeException("user with id $userId is not creator of project with id $projectId")

        // Проверяем, что проект ещё не удален
        if (userProject.deletionDate != null)
            throw RuntimeException("project $projectId is already deleted")

        // Устанавлиеваем в поле deletionDate текущую дату
        userProject.deletionDate = LocalDate.now()
        projectRepository.save(userProject)
    }
}