package ru.ivan.students.dto.request

import ru.ivan.students.domian.CV

data class CVRequest(
    val nameCV: String,
    val aboutInfo: String,
    val school: String,
    val university: String,
    val workStatus: String,
    val citizenship: String,
    val language: String,
    val workSchedule: String,
    val skill: String,
    val busyness: String
)

fun CVRequest.toEntity(cvId: String? = null): CV {
    return CV(
        id = cvId,
        nameCV = this.nameCV,
        citizenship = this.citizenship,
        aboutInfo = this.aboutInfo,
        language = this.language,
        workStatus = this.workStatus,
        school = this.school,
        skill = this.skill,
        busyness = this.busyness,
        university = this.university,
        workSchedule = this.workSchedule
    )
}
