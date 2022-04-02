package ru.ivan.students.dto.request

/** Принимает инфу от пользователя
 *
 */
data class AccountRequest(
    val name: String,
    val suraname: String,
    val status: String
)
