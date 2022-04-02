package ru.ivan.students.controller

import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.hamcrest.Matchers.containsString
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class AccountControllerTest {



    private val port=8080

    @Autowired
    private val restTemplate: TestRestTemplate = TestRestTemplate()

    @Autowired
    private val controller: AccountController? = null

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