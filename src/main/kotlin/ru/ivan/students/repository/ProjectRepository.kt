package ru.ivan.students.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.ivan.students.domian.Project
import java.sql.ResultSet
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

    @Transactional
    @Query(
        value = "SELECT p.project_id FROM projects p LEFT JOIN accounts_projects_likes l USING (project_id) where l.project_id is null",
        nativeQuery = true
    )
    fun getIdsWithZero(): Collection<String>

    @Transactional
    @Query(
        value = "SELECT COUNT(*) FROM accounts_projects_likes WHERE project_id =:id",
        nativeQuery = true
    )
    fun countLikesById(@Param("id") id: String): Int

    @Transactional
    @Query(
        value = "SELECT COUNT(*) FROM accounts_projects_views WHERE project_id =:id",
        nativeQuery = true
    )
    fun countViewsById(@Param("id") id: String): Int

    fun findByIdAndDeletionDateNull(id: String): Optional<Project>

    fun findAllByDeletionDateNull(): List<Project>

    @EntityGraph(attributePaths = ["accounts", "accounts.account"])
    fun findUsingEntityGraphAccountById(@Param("id") id: String): Optional<Project>
}