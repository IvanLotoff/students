package ru.ivan.students.domian

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
@Table(name = "cvs")
data class CV(
    @Id
    @Column(name = "cv_id", updatable = false)
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    val id: String? = null,
    val nameCV: String,
    val aboutInfo: String,
    val school: String,
    val university: String,
    val workStatus: String,
    val citizenship: String,
    val language: String,
    val workSchedule: String,
    val skill: String,
    val busyness : String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CV

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
