package ru.ivan.students.mapper

import org.springframework.stereotype.Component
import ru.ivan.students.domian.Account
import ru.ivan.students.dto.request.AccountRequest

@Component
class AccountDTOToAccount {
    fun map(accountRequest: AccountRequest) = Account(
        name = accountRequest.name,
        surname = accountRequest.suraname,
        status = accountRequest.status,
    )
}