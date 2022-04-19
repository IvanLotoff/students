package ru.ivan.students.dto.response

data class UserResponse(
    val email: String,
    val nickname: String,
    val phoneNumber: String?,
    val firstName: String,
    val lastName: String,
    val telegram: String?,
)
