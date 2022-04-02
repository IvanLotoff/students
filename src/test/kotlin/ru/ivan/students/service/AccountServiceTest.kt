package ru.ivan.students.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.ivan.students.domian.Account


@SpringBootTest
internal class AccountServiceTest {

    @Autowired
    var accountService: AccountService? = null
    var account: Account = Account()

    @BeforeEach
    fun setUp() {
        account.id = "53"
    }

    @AfterEach
    fun tearDown() {
        account = Account()
    }

    @Test
    fun existsById() {
    }

    /**
     * Check search account by id
     */
    @Test
    fun findAccountById() {
        var str = account.id.toString()
        accountService?.createAccount(account)
        val res = accountService?.findAccountById(str)?.get()
        assertEquals(account.id, res?.id)
    }

    /**
     * Check create account
     */
    @Test
    fun createAccount() {
        val acc2 = accountService?.createAccount(account)
        assertEquals(acc2, account)
    }
}