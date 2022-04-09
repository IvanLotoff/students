package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Project
import ru.ivan.students.repository.AccountRepository
import ru.ivan.students.repository.ProjectRepository

@Service
class ProjectService {
    @Autowired
    private lateinit var projectRepository: ProjectRepository
    private lateinit var accountRepository: AccountRepository

    fun createProject(project: Project): Project {
        return projectRepository.save(project)
    }

    /***
     * Logic of recommendations
     */
    fun searchRecommendedProjects(accountId: String): MutableList<Project> {
        var account = accountRepository.findById(accountId).get()

        var tags = account.likes!!.flatMap { it.tags }.distinct()

        // Get all another project which are not the same
        var allProjects = projectRepository.findAll()
        allProjects.removeAll { account.likes!!.contains(it) }

        var recommends = mutableListOf<Project>()
        for (it in allProjects) {
            for (el in it.tags)
                if (tags.contains(el)) {
                    recommends.add(it)
                }
        }

        return recommends
    }
}