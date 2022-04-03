package ru.ivan.students.dto.request

/** Принимает инфу от пользователя
 *
 */
data class AccountRequest(
    val email: String,
    val nickName: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val surname: String,
    val status: String,
)
