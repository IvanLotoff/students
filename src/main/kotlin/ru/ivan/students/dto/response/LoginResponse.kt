package ru.ivan.students.dto.response

data class LoginResponse(
    val userResponse: UserResponse,
    val token: String
)
