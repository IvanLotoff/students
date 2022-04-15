package ru.ivan.students.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme


@OpenAPIDefinition(info = Info(title = "Ассистент для студентов", description = "Курсовой проект БПИ193", version = "v1"))
@SecurityScheme(
    name = "apiKey",
    scheme = "bearer",
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP,
    `in` = SecuritySchemeIn.HEADER
)
class OpenApiConfig