package ru.ivan.students.mapper

import org.springframework.stereotype.Component
import ru.ivan.students.domian.CV
import ru.ivan.students.dto.CVDto

@Component
class CVDtoToCV {
    fun map(cvDto: CVDto): CV {
        return CV(
            nameCV = cvDto.nameCV,
            citizenship = cvDto.citizenship,
            aboutInfo = cvDto.aboutInfo,
            language = cvDto.language,
            workStatus = cvDto.workStatus,
            school = cvDto.school,
            skill = cvDto.skill,
            busyness = cvDto.busyness,
            university = cvDto.university,
            workSchedule = cvDto.workSchedule
        )
    }
}