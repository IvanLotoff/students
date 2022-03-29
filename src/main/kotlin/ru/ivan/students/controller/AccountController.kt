package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.domian.Account
import ru.ivan.students.dto.AccountDTO
import ru.ivan.students.mapper.AccountDTOToAccount
import ru.ivan.students.service.AccountService
import java.security.Principal

@RestController
@RequestMapping("/api/account")
class AccountController {
    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var accountDTOToAccount: AccountDTOToAccount

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "security_auth")
    fun saveAccount(principal: Principal, @RequestBody accountDto: AccountDTO): ResponseEntity<Account> {
        val account = accountDTOToAccount.map(accountDto)
        if(accountService.existsById(principal.name))
            throw RuntimeException("Already reigstered")
        account.id = principal.name
        return ResponseEntity.ok(accountService.createAccount(account))
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "security_auth")
    fun getMyAccount(principal: Principal): ResponseEntity<Account> {
        return accountService.findAccountById(principal.name).map {
            ResponseEntity.ok(it)
        }.orElseThrow {  throw RuntimeException("not found") }
    }
}