package ru.ivan.students.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.ivan.students.domian.Tag

@Repository
interface TagRepository : JpaRepository<Tag, String> {

}