package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
     * Logic of recommendations
     */
    fun searchRecommendedProjects(accountId: String): List<ProjectResponse> {
        var account = accountRepository.findById(accountId).get()

        var tags = account.likes!!.flatMap { it.tags }.distinct()

        // Get all another project which are not the same
        var allProjects = projectRepository.findAll()
        allProjects.removeAll { account.likes!!.contains(it) }

        var recommends = mutableListOf<ProjectResponse>()
        for (it in allProjects) {
            for (el in it.tags)
                if (tags.contains(el)) {
                    recommends.add(it.toResponse())
                }
        }

        return recommends
    }
}