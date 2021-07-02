package br.com.edu.autor.model

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface AutorRepository: JpaRepository<Autor, Long>