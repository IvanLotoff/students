package ru.ivan.students.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.ivan.students.domian.Account

@Repository
interface AccountRepository: JpaRepository<Account, String> {
    fun existsAccountById(id: String): Boolean
}