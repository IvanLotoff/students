package ru.ivan.students.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.ivan.students.domian.Project
import java.util.*

@Repository
interface ProjectRepository : PagingAndSortingRepository<Project, String> {
    //
//    override fun findAll(pageable: Pageable): Page<Project>
    fun findByCreatorIdAndDeletionDateNull(str: String): List<Project>

    @Transactional
    @Query(
        value = "SELECT p.* FROM accounts_projects_likes l INNER JOIN projects p USING (project_id) GROUP BY p.project_id ORDER BY COUNT(*) DESC",
        nativeQuery = true
    )
    fun orderByLikes(): Collection<Project>

    @Transactional
    @Query(
        value = "SELECT project_id FROM accounts_projects_likes GROUP BY project_id ORDER BY COUNT(*) DESC",
        nativeQuery = true
    )
    fun orderIdsByLikes(): Collection<String>

    fun findByIdAndDeletionDateNull(id: String): Optional<Project>

    fun findAllByDeletionDateNull(): List<Project>
}