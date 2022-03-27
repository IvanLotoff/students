package ru.ivan.students.mapper

import org.springframework.stereotype.Component
import ru.ivan.students.domian.Account
import ru.ivan.students.dto.AccountDTO

@Component
class AccountDTOToAccount {
    fun map(accountDTO: AccountDTO) = Account(
        name = accountDTO.name,
        purpose = accountDTO.purpose,
        description = accountDTO.description,
        deadlineProjectDateFrom = accountDTO.deadlineProjectDateFrom,
        deadlineTeamDateTo = accountDTO.deadlineTeamDateTo,
        deadlineProjectDateTo = accountDTO.deadlineProjectDateTo,
        status = accountDTO.status,
        participantsNumber = accountDTO.participantsNumber,
        recordingPeriod = accountDTO.recordingPeriod
    )
}