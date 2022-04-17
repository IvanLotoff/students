package ru.ivan.students.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.ivan.students.domian.Project

@Repository
interface ProjectRepository : PagingAndSortingRepository<Project, String> {
    //
//    override fun findAll(pageable: Pageable): Page<Project>
    fun findByCreatorId(str: String): List<Project>

    @Transactional
    @Query(
        value = "SELECT p.* FROM accounts_projects_likes l INNER JOIN projects p USING (project_id) GROUP BY p.project_id ORDER BY COUNT(*) DESC",
        nativeQuery = true
    )
    fun orderByLikes(): Collection<Project>
}