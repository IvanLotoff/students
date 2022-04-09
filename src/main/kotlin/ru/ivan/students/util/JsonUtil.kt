package ru.ivan.students.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature


fun Any.toJson(): String {
    val objectMapper = ObjectMapper()
    return objectMapper.writeValueAsString(this)
}