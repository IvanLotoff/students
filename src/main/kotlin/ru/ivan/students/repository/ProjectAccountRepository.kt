package ru.ivan.students.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.ivan.students.domian.Project
import ru.ivan.students.domian.ProjectAccount

@Repository
interface ProjectAccountRepository : PagingAndSortingRepository<ProjectAccount, String> {

}