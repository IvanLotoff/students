package ru.ivan.students.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.ivan.students.domian.Project

@Repository
interface ProjectRepository : PagingAndSortingRepository<Project, String> {

    fun findByCreatorId(str: String): List<Project>
}