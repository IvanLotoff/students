package ru.ivan.students.domian

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.ivan.students.dto.response.ProjectResponse
import ru.ivan.students.dto.response.TagResponse
import javax.persistence.*

@Entity
@Table(name = "tags")
data class Tag(
    @Id
    @Column(name = "tag_id", updatable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    val id: String? = null,
    val name: String,
    val about: String? = null,


    @ManyToOne(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
    @JoinColumn(name = "project_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    var project: Project?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Tag

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }


}

fun Tag.toResponse(): TagResponse {
    return TagResponse(
        id = this.id!!,
        name = this.name,
        about = this.about
    )
}
