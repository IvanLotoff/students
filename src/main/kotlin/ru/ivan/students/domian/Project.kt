package ru.ivan.students.domian

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.*
import com.fasterxml.jackson.annotation.JsonProperty.Access.*
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.validation.constraints.Size

/**
 * TODO: наверное стоит подумать, что хранить в проекте, наверное ссылки на контакты, описание и навыки, ссылка на руководителя. Но имя и фамилия не нужны!
 */
@Entity(name = "projects")
data class Project(
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    val id: String? = null,
    val interests: String,

    val description: String,
    val telegram: String,


    @OneToMany(mappedBy = "project", cascade = arrayOf(CascadeType.ALL))
    val tags: MutableList<Tag> = mutableListOf(),
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
