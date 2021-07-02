package br.com.edu.livro.model

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface LivroRepository: JpaRepository<Livro, Long>