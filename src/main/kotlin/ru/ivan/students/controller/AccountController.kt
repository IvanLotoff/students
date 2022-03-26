package ru.ivan.students.controller

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.ivan.students.domian.Account
import ru.ivan.students.service.AccountService
import java.security.Principal

@RestController
@RequestMapping("/api/account")
class AccountController {
    @Autowired
    private lateinit var accountService: AccountService

    @PostMapping("/save")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "security_auth")
    fun getUserInfo(principal: Principal, @RequestBody account: Account): ResponseEntity<Account> {
        //val keycloakAuthenticationToken = principal as KeycloakAuthenticationToken
        account.userId = principal.name
        return ResponseEntity.ok(accountService.createAccount(account))
    }
}