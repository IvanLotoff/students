package ru.ivan.students.dto.response

data class TagResponse(
    val id: String?,
    val name: String,
    val about: String? = null
)
