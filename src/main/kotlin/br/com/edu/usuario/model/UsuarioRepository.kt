package br.com.edu.usuario.model

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface UsuarioRepository: JpaRepository<Usuario, Long> {
    fun existsByLogin(login: String): Boolean
}