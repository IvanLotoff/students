package ru.ivan.students.repository

import org.springframework.data.domain.Page
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.ivan.students.domian.Project
import org.springframework.data.domain.Pageable

@Repository
interface ProjectRepository : PagingAndSortingRepository<Project, String> {
//
//    override fun findAll(pageable: Pageable): Page<Project>
    fun findByCreatorId(str: String): List<Project>
}