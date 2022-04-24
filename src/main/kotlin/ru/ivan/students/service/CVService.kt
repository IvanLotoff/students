package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import ru.ivan.students.domian.toResponse
import ru.ivan.students.dto.request.CVRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.dto.response.CVResponse
import ru.ivan.students.mapper.CVConverter
import ru.ivan.students.repository.AccountRepository
import ru.ivan.students.repository.CVRepository

@Service
class CVService {
    @Autowired
    private lateinit var converter: CVConverter

    @Autowired
    private lateinit var cvRepository: CVRepository

    @Autowired
    private lateinit var accountRepository: AccountRepository

    fun saveCV(CVRequest: CVRequest, userId: String): CVResponse {
        val cv = CVRequest.toEntity()
        var account = accountRepository.findById(userId).orElseThrow {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't find user $userId")
        }
        cv.account = account
        account.cvs.add(cv)
        val save = cvRepository.save(cv)
        accountRepository.save(account)
        return save.toResponse()
    }

    fun updateCV(cvRequest: CVRequest, userId: String, cvId: String): CVResponse {
        return cvRepository.save(
            cvRequest.toEntity(cvId)
        ).toResponse()
    }

    fun getAllCvFromAccount(accountId: String): List<CVResponse> {
        var account = accountRepository.findById(accountId).orElseThrow {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't find user $accountId")
        }
        return account.cvs.map { cv -> cv.toResponse() };
    }

    fun showAll() = converter.toListOfCVResponse(cvRepository.findAll())
}