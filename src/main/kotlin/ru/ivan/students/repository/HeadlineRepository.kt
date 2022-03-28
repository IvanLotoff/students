package ru.ivan.students.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.ivan.students.domian.CV
import ru.ivan.students.domian.Headline
import ru.ivan.students.domian.Project

@Repository
interface HeadlineRepository: JpaRepository<Headline, String> {
}