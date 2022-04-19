package ru.ivan.students.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertTrue

@SpringBootTest
internal class StepikServiceTest {
    @Autowired
    var stepikService: StepikService = StepikService();

    @Autowired
    var newsService = NewsService();

    @Test
    internal fun getTestStepik() {
        assertTrue { stepikService.getCourses(1) }
    }

    @Test
    internal fun getTestNews() {
        assertTrue { newsService.getCourses(1) }
    }
}