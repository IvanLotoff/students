package ru.ivan.students.domian

import org.hibernate.Hibernate
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @Column(name = "account_id", updatable = false)
    var id: String? = null,

    @Size(max = 5)
    @OneToMany(mappedBy = "account", cascade = [CascadeType.ALL])
    val cvs: MutableList<CV> = mutableListOf(),

    @OneToMany(mappedBy = "account")
    val likes: MutableList<ProjectAccount> = mutableListOf(),

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "accounts_projects_views",
        joinColumns = [JoinColumn(name = "account_id")],
        inverseJoinColumns = [JoinColumn(name = "project_id")]
    )
    val views: MutableList<Project> = mutableListOf(),

    @OneToMany
    val courses: List<Course> = listOf()
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
