package br.com.edu.usuario.model

import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
class Usuario(
    @field:NotBlank @field:Email val login: String,
    @field:NotBlank @field:Size(min = 6) val senha: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    val dataCriacao: OffsetDateTime? = null
}