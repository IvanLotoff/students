package ru.ivan.students.domian

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import ru.ivan.students.dto.response.ProjectResponse
import ru.ivan.students.dto.response.TagResponse
import java.time.LocalDate
import javax.persistence.*

@Entity(name = "projects")
data class Project(
    @Id
    @Column(name = "project_id", updatable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    val id: String? = null,
    var title: String,
    @Lob @Type(type = "org.hibernate.type.TextType")
    var description: String,
    var communication: String,
    var creatorId: String? = null,

    @OneToMany(orphanRemoval = true, mappedBy = "project", cascade = arrayOf(CascadeType.ALL))
    var accounts: MutableList<ProjectAccount> = mutableListOf(),

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "views")
    var accountsView: MutableList<Account> = mutableListOf(),

    @OneToMany(orphanRemoval = true, mappedBy = "project", cascade = arrayOf(CascadeType.ALL))
    var tags: MutableList<Tag> = mutableListOf(),

    var deletionDate: LocalDate? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Project

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}

fun Project.toResponse(): ProjectResponse {
    return ProjectResponse(
        id = this.id!!,
        title = this.title,
        description = this.description,
        communication = this.communication,
        creatorId = this.creatorId!!,
        tags = this.toListOfTagResponse()
    )
}

fun Project.toListOfTagResponse(): List<TagResponse> {
    return this.tags.map { tag ->
        tag.toResponse()
    }
}
