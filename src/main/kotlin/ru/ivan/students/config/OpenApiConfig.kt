package ru.ivan.students.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.OAuthFlow
import io.swagger.v3.oas.annotations.security.OAuthFlows
import io.swagger.v3.oas.annotations.security.OAuthScope
import io.swagger.v3.oas.annotations.security.SecurityScheme

@OpenAPIDefinition(info = Info(title = "Ассистент для студентов", description = "Курсовой проект БПИ193", version = "v1"))
@SecurityScheme(
    name = "security_auth",
    type = SecuritySchemeType.OAUTH2,
    flows = OAuthFlows(
        authorizationCode = OAuthFlow(
            authorizationUrl = "http://localhost:8484/auth/realms/test_realm/protocol/openid-connect/auth",
            tokenUrl = "http://localhost:8484/auth/realms/test_realm/protocol/openid-connect/token",
            scopes = [OAuthScope(name = "read", description = "read scope"), OAuthScope(
                name = "write",
                description = "write scope"
            )]
        )
    )
)
class OpenApiConfig