package br.com.edu.categoria

import br.com.edu.CategoriaGrpcServiceGrpc
import br.com.edu.CategoriaRequest
import br.com.edu.CategoriaResponse
import br.com.edu.categoria.model.Categoria
import br.com.edu.categoria.model.CategoriaRepository
import br.com.edu.shared.exception.JaExistente
import br.com.edu.shared.interceptors.ErrorAdvice
import io.grpc.stub.StreamObserver
import io.micronaut.http.client.annotation.Client
import javax.inject.Singleton
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Singleton
@ErrorAdvice
class CategoriaEndpoint(val validator: Validator, val categoriaRepository: CategoriaRepository) :
    CategoriaGrpcServiceGrpc.CategoriaGrpcServiceImplBase() {

    override fun cadastrar(request: CategoriaRequest, responseObserver: StreamObserver<CategoriaResponse>) {
        val categoria = request.toCategoria(validator)

        if (categoriaRepository.existsByNome(categoria.nome)) {
            throw JaExistente("Categoria ja existe")
        }

        categoriaRepository.save(categoria)

        responseObserver.onNext(CategoriaResponse.newBuilder().setId(categoria.id!!).setNome(categoria.nome).build())
        responseObserver.onCompleted()
    }

}

fun CategoriaRequest.toCategoria(validator: Validator): Categoria {
    val categoria = Categoria(nome = nome)

    val errors = validator.validate(categoria)

    if (errors.isNotEmpty()) {
        throw ConstraintViolationException(errors)
    }

    return categoria
}