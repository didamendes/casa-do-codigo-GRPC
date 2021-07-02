package br.com.edu.livro

import br.com.edu.LivroGrpcServiceGrpc
import br.com.edu.LivroRequest
import br.com.edu.LivroResponse
import br.com.edu.autor.model.AutorRepository
import br.com.edu.categoria.model.CategoriaRepository
import br.com.edu.livro.model.Livro
import br.com.edu.livro.model.LivroRepository
import br.com.edu.shared.exception.NenhumResultadoEncontrado
import br.com.edu.shared.interceptors.ErrorAdvice
import io.grpc.stub.StreamObserver
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Singleton
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Singleton
@ErrorAdvice
class LivroEndpoint(
    val validator: Validator,
    val autorRepository: AutorRepository,
    val livroRepository: LivroRepository,
    val categoriaRepository: CategoriaRepository
) : LivroGrpcServiceGrpc.LivroGrpcServiceImplBase() {

    override fun cadastrar(request: LivroRequest, responseObserver: StreamObserver<LivroResponse>) {
        val livro = request.toLivro(validator, autorRepository, categoriaRepository)

        livroRepository.save(livro)

        responseObserver.onNext(
            LivroResponse.newBuilder().setId(livro.id!!).setTitulo(livro.titulo).setResumo(livro.resumo)
                .setSumario(livro.sumario).setPreco(livro.preco.toString()).setPaginas(livro.paginas)
                .setIsbn(livro.isbn).setDataPublicacao(livro.dataPublicacao.toString()).setAutor(livro.autor.nome)
                .setCategoria(livro.categoria.nome).build()
        )
        responseObserver.onCompleted()
    }

}

fun LivroRequest.toLivro(
    validator: Validator,
    autorRepository: AutorRepository,
    categoriaRepository: CategoriaRepository
): Livro {

    val autor = autorRepository.findById(idAutor)
        .orElseThrow { throw NenhumResultadoEncontrado("Autor inexistente") }

    val categoria = categoriaRepository.findById(idCategoria)
        .orElseThrow { throw NenhumResultadoEncontrado("Categoria não encontrada") }

    val livro = Livro(
        titulo = titulo,
        resumo = resumo,
        sumario = sumario,
        preco = BigDecimal(preco),
        paginas = paginas,
        isbn = isbn,
        dataPublicacao = LocalDate.parse(dataPublicacao, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        autor = autor,
        categoria = categoria
    )

    val errors = validator.validate(livro)

    if (errors.isNotEmpty()) {
        throw ConstraintViolationException(errors)
    }

    return livro
}