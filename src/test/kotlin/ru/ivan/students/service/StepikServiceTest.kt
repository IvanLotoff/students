package ru.ivan.students.service

import org.junit.jupiter.api.Test

internal class StepikServiceTest {
    var stepikService:StepikService= StepikService();
    @Test
    internal fun getTest() {
        println(stepikService.getAccessToken())
        //stepikService.sendGet()
    }
}