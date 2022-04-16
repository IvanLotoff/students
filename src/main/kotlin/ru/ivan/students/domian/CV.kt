package ru.ivan.students.domian

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import ru.ivan.students.dto.response.CVResponse
import javax.persistence.*

@Entity
@Table(name = "cvs")
data class CV(
    @Id
    @Column(name = "cv_id", updatable = false)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    val id: String? = null,
    val nameCV: String,
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    val aboutInfo: String,
    val school: String,
    val university: String,
    val workStatus: String,
    val citizenship: String,
    val language: String,
    val workSchedule: String,
    val skill: String,
    val busyness: String,

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "account_id")
    var account: Account? = null
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

fun CV.toResponse(): CVResponse {
    return CVResponse(
        id = this.id!!,
        nameCV = this.nameCV,
        citizenship = this.citizenship,
        aboutInfo = this.aboutInfo,
        language = this.language,
        workStatus = this.workStatus,
        school = this.school,
        skill = this.skill,
        busyness = this.busyness,
        university = this.university,
        workSchedule = this.workSchedule
    )
}
