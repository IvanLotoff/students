package ru.ivan.students.domian

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import ru.ivan.students.dto.response.CourseResponse
import javax.persistence.*

@Entity
@Table(name = "courses")
data class Course(
    @Id
    @Column(name = "course_id", updatable = false)
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    val id: String? = null,
    val name: String,
    val spec: String,
    val seatsNumber: Int,
    val teacher: String,
    val source: String,
    var userId: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Course

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}

fun Course.toResponse(): CourseResponse {
    return CourseResponse(
        id = this.id,
        name = this.name,
        spec = this.spec,
        seatsNumber = this.seatsNumber,
        teacher = this.teacher,
        source = this.source
    )
}
