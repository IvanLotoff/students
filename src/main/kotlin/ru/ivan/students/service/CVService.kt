package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.CV
import ru.ivan.students.domian.toResponse
import ru.ivan.students.dto.request.CVRequest
import ru.ivan.students.dto.request.ProjectRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.dto.response.CVResponse
import ru.ivan.students.dto.response.ProjectResponse
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
        cv.account = accountRepository.getById(userId)
        val save = cvRepository.save(cv)
        return save.toResponse()
    }

    fun updateCV(cvRequest: CVRequest, userId: String, cvId: String): CVResponse {
        return cvRepository.save(
            cvRequest.toEntity(cvId)
        ).toResponse()
    }

    fun getAllCvFromAccount(accountId: String): List<CVResponse> {
        var account = accountRepository.findById(accountId).orElseThrow {
            throw RuntimeException("Can't find user $accountId")
        }
        return account.cvs.map { cv -> cv.toResponse() };
    }

    fun showAll() = converter.toListOfCVResponse(cvRepository.findAll())
}