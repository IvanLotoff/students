package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Account
import ru.ivan.students.domian.CV
import ru.ivan.students.domian.Project
import ru.ivan.students.repository.AccountRepository
import ru.ivan.students.repository.ProjectRepository

@Service
class ProjectService {
    @Autowired
    private lateinit var  projectRepository: ProjectRepository

    fun createProject(project: Project): Project {
        return projectRepository.save(project)
    }

    fun searchRecommendedProjects(account: Account): MutableList<Project> {
        return  mutableListOf<Project>()
    }
}