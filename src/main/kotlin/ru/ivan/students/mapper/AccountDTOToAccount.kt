package ru.ivan.students.mapper

import org.springframework.stereotype.Component
import ru.ivan.students.domian.Account
import ru.ivan.students.dto.request.AccountRequest

@Component
class AccountDTOToAccount {
    fun map(accountRequest: AccountRequest) = Account(
        name = accountRequest.name,
        purpose = accountRequest.purpose,
        description = accountRequest.description,
        deadlineProjectDateFrom = accountRequest.deadlineProjectDateFrom,
        deadlineTeamDateTo = accountRequest.deadlineTeamDateTo,
        deadlineProjectDateTo = accountRequest.deadlineProjectDateTo,
        status = accountRequest.status,
        participantsNumber = accountRequest.participantsNumber,
        recordingPeriod = accountRequest.recordingPeriod
    )
}