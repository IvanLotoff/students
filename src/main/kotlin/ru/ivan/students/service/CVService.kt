package ru.ivan.students.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ivan.students.domian.CV
import ru.ivan.students.dto.CVDto
import ru.ivan.students.mapper.CVDtoToCV
import ru.ivan.students.repository.CVRepository

@Service
class CVService {
    @Autowired
    private lateinit var converter: CVDtoToCV
    @Autowired
    private lateinit var cvRepository: CVRepository

    fun saveCV(cvDto: CVDto): CV {
        val cv = converter.map(cvDto)
        return cvRepository.save(cv)
    }

    fun showAll() = cvRepository.findAll()
}