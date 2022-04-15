package ru.ivan.students.annotation

/**
 * Маркерная аннотация, которая предупреждает, что метод
 * должен вызываться только при старте
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ForStartupOnly
