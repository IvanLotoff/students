package ru.ivan.students.config

import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.admin.client.Keycloak
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

@KeycloakConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class KeycloakCjonfig : KeycloakWebSecurityConfigurerAdapter() {
    /**
     * Нужно добавить session authentication strategy bean тип которого должен быть RegisterSessionAuthenticationStrategy
     * для public или confidential клиентов и NullAuthenticatedSessionStrategy для bearer-only приложений.
     *
     * SessionAuthenticationStrategy имеет один метод onAuthenticate, в данном случае в нем ничего не происходит
     */
    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy = NullAuthenticatedSessionStrategy()


    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

//    @Bean
//    fun keycloakConfigResolver(): KeycloakConfigResolver {
//        return KeycloakSpringBootConfigResolver()
//    }

//    override fun configure(http: HttpSecurity) {
//        super.configure(http)
//        http
//            .authorizeRequests()
//            .antMatchers("/api/public/**").permitAll()
//            .anyRequest().fullyAuthenticated()
//    }

    override fun configure(http: HttpSecurity) {

        http
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/swagger*/**", "/v3/api-docs", "/actuator/**").permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun keycloak() : Keycloak =  Keycloak.getInstance(
                "http://localhost:8484/auth",
                "master",
                "admin",
                "admin",
                "admin-cli");

}