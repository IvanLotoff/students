package ru.ivan.students.domian

import org.hibernate.Hibernate
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class ProjectAccountId(
    @Column(name = "account_id")
    private var accountId: String,

    @Column(name = "project_id")
    private var projectId: String
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProjectAccountId

        return accountId == other.accountId && projectId == other.projectId
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $accountId )"
    }

    companion object {
        private const val serialVersionUID = 3048626870472793205L
    }
}
