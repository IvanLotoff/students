package ru.ivan.students.domian

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "headlines")
data class Headline(
    @Id
    @Column(name = "headline_id", updatable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    val id: String? = null,
    val name: String,
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    val text: String,
    val tag: String,
    val source: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Headline

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }


}
