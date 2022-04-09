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
    val email: String? = null,
    val nickName: String? = null,
    val phoneNumber: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val surname: String? = null,
    val status: String? = null,



    @Size(max = 5)
    @OneToMany(mappedBy = "account",cascade = [CascadeType.ALL])
    val cvs: MutableList<CV> = mutableListOf(),

    @JoinColumn(name = "account_id")
    @OneToMany()
    val likes: MutableList<Project> = mutableListOf(),

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
