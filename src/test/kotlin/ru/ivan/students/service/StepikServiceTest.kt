package ru.ivan.students.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class StepikServiceTest {
    @Autowired
    var stepikService:StepikService= StepikService();
    @Test
    internal fun getTest() {
        //println(stepikService.getAccessToken())
        //println(stepikService.getCourse())
        println(stepikService.getCourses())
        //stepikService.sendGet()
    }
}