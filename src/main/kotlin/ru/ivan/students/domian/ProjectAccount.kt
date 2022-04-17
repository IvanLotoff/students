package ru.ivan.students.domian

import org.hibernate.Hibernate
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "accounts_projects_likes")
data class ProjectAccount(
    @EmbeddedId
    var id:ProjectAccountId,

    @ManyToOne
    @JoinColumn(name = "projectL_id", referencedColumnName = "project_id")
    var project: Project,

    @ManyToOne
    @JoinColumn(name = "accountL_id", referencedColumnName = "account_id")
    var account: Account,

    val createdOn: LocalDate = LocalDate.now()

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProjectAccount

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = ${account.id} )"
    }
}
