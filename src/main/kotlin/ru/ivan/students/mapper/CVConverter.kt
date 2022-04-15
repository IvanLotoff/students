package ru.ivan.students.mapper

import org.springframework.stereotype.Component
import ru.ivan.students.domian.CV
import ru.ivan.students.dto.request.CVRequest
import ru.ivan.students.dto.response.CVResponse

@Component
class CVConverter {
    fun toListOfCVResponse(cvList: List<CV>): List<CVResponse> {
        return cvList.map { cv ->
            CVResponse(
                id = cv.id!!,
                nameCV = cv.nameCV,
                citizenship = cv.citizenship,
                aboutInfo = cv.aboutInfo,
                language = cv.language,
                workStatus = cv.workStatus,
                school = cv.school,
                skill = cv.skill,
                busyness = cv.busyness,
                university = cv.university,
                workSchedule = cv.workSchedule
            )
        }
    }
}