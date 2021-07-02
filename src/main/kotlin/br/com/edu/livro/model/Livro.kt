package br.com.edu.livro.model

import br.com.edu.autor.model.Autor
import br.com.edu.categoria.model.Categoria
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Introspected
@Entity
class Livro(
    @field:NotBlank val titulo: String,
    @field:NotBlank val resumo: String,
    @field:NotBlank val sumario: String,
    @field:NotNull val preco: BigDecimal,
    @field:NotNull val paginas: Int,
    @field:NotBlank val isbn: String,
    @field:FutureOrPresent val dataPublicacao: LocalDate,
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    val autor: Autor,
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    val categoria: Categoria
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}