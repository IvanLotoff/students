package ru.ivan.students.mapper

import org.springframework.stereotype.Component
import ru.ivan.students.domian.CV
import ru.ivan.students.dto.request.CVRequest
import ru.ivan.students.dto.response.CVResponse

@Component
class CVConverter {
    fun toEntity(CVRequest: CVRequest): CV {
        return CV(
            nameCV = CVRequest.nameCV,
            citizenship = CVRequest.citizenship,
            aboutInfo = CVRequest.aboutInfo,
            language = CVRequest.language,
            workStatus = CVRequest.workStatus,
            school = CVRequest.school,
            skill = CVRequest.skill,
            busyness = CVRequest.busyness,
            university = CVRequest.university,
            workSchedule = CVRequest.workSchedule
        )
    }

    fun toResponse(cv: CV): CVResponse {
        return CVResponse(
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

    fun toListOfCVResponse(cvList: List<CV>): List<CVResponse> {
        return cvList.map { cv ->
            CVResponse(
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