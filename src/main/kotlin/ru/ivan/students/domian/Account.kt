package ru.ivan.students.domian

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @Column(name = "account_id", updatable = false)
    var id: String? = null,
    val name: String? = null,
    val purpose: String? = null,
    val description: String? = null,
    val deadlineProjectDateFrom: String? = null,
    val participantsNumber: Int? = null,
    val recordingPeriod: String? = null,
    val status: String? = null,
    val deadlineProjectDateTo: String? = null,
    val deadlineTeamDateTo: String? = null,

    @OneToMany(mappedBy = "account")
    val cvs: MutableList<CV> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
        @JoinTable(
                name = "accounts_projects",
                joinColumns = [JoinColumn(name = "account_id")],
                inverseJoinColumns = [JoinColumn(name = "project_id")]
        )
        @JsonProperty(access = WRITE_ONLY)
        val likes: Set<Project>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Account

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
