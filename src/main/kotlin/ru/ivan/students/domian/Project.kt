package ru.ivan.students.domian

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.*
import com.fasterxml.jackson.annotation.JsonProperty.Access.*
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity(name = "projects")
data class Project(
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    val id: String,
    val interests: String,
    val surname: String,
    val name: String,
    val password: String,
    val telegram: String,
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "likes")
    @JsonProperty(access = WRITE_ONLY)
    val likedProjects: Set<Account>?
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
