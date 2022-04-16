package ru.ivan.students.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.ivan.students.domian.Project

@Repository
interface ProjectRepository : PagingAndSortingRepository<Project, String> {
    //
//    override fun findAll(pageable: Pageable): Page<Project>
    fun findByCreatorId(str: String): List<Project>

    @Query(
        value = "SELECT id FROM accounts_projects_likes  GROUP BY project_id ORDER BY COUNT(*) DESC",
        nativeQuery = true
    )
    fun orderByLikes(): Collection<String>
}