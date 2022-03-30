package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.CV
import ru.ivan.students.dto.request.CVRequest
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
        val cv = converter.toEntity(CVRequest)
        cv.account = accountRepository.getById(userId)
        val save = cvRepository.save(cv)
        return converter.toResponse(save)
    }

    fun showAll() = converter.toListOfCVResponse(cvRepository.findAll())
}