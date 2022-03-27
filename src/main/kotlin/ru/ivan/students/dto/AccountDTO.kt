package ru.ivan.students.dto

/** Принимает инфу от пользователя
 *
 */
data class AccountDTO(
    val name: String,
    val purpose: String,
    val description: String,
    val deadlineProjectDateFrom: String,
    val participantsNumber: Int,
    val recordingPeriod: String,
    val status: String,
    val deadlineProjectDateTo: String,
    val deadlineTeamDateTo: String
)
