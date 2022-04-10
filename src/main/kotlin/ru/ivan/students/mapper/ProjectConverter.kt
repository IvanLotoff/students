package ru.ivan.students.mapper

import org.springframework.stereotype.Component
import ru.ivan.students.domian.Tag
import ru.ivan.students.dto.request.TagRequest
import ru.ivan.students.dto.request.toEntity

@Component
class ProjectConverter {
    fun toListOfTagToRequest(idProject: String, list: List<TagRequest>): List<Tag> {
        return list.map { tag ->
            tag.toEntity(idProject)
        }
    }
}