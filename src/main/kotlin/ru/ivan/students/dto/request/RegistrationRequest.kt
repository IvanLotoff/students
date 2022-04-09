package ru.ivan.students.dto.request

data class RegistrationRequest(
    val email: String,
    val nickname: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String,
    val telegram: String,
    val password: String
)
