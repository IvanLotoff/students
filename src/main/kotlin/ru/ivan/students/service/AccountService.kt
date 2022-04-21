package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.Account
import ru.ivan.students.repository.AccountRepository

@Service
class AccountService {
    @Autowired
    private lateinit var accountRepository: AccountRepository

    fun existsById(id: String) = accountRepository.existsAccountById(id)

    fun findAccountById(id: String) = accountRepository.findById(id)

    fun createAccount(account: Account): Account {
        return accountRepository.save(account)
    }

    fun deleteAllAccounts() {
        accountRepository.deleteAll()
    }
}