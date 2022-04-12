package ru.ivan.students.mapper

import org.springframework.stereotype.Component
import ru.ivan.students.domian.Tag
import ru.ivan.students.domian.toResponse
import ru.ivan.students.dto.request.TagRequest
import ru.ivan.students.dto.request.toEntity
import ru.ivan.students.dto.response.TagResponse

@Component
class ProjectConverter {
    fun toListOfTagToRequest( list: List<TagRequest>): List<Tag> {
        return list.map { tag ->
            tag.toEntity()
        }
    }


    fun toListOfTagToResponse(list: List<Tag>): List<TagResponse> {
        return list.map { tag ->
            tag.toResponse()
        }
    }
}