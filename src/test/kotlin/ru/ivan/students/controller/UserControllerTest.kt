package ru.ivan.students.controller

import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UserControllerTest {



    @LocalServerPort
    private val port=8080

    @Autowired
    private val restTemplate: TestRestTemplate = TestRestTemplate()

    @Autowired
    private val controller: UserController? = null

    @Test
    fun saveAccount() {
        assertThat(controller).isNotNull;
    }

    @Test
    fun getMyAccount() {

        assertThat(
            restTemplate!!.getForObject(
                "http://localhost:$port/",
                String::class.java
            )
        ).contains("Hello, World")
    }
}