package br.com.edu.autor

import br.com.edu.AutorGrpcServiceGrpc
import br.com.edu.AutorRequest
import br.com.edu.AutorResponse
import br.com.edu.autor.model.Autor
import br.com.edu.autor.model.AutorRepository
import br.com.edu.shared.interceptors.ErrorAdvice
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Singleton
@ErrorAdvice
class AutorEndpoint(val validator: Validator, val autorRepository: AutorRepository) :
    AutorGrpcServiceGrpc.AutorGrpcServiceImplBase() {

    override fun cadastrar(request: AutorRequest, responseObserver: StreamObserver<AutorResponse>) {
        val autor = request.toAutor(validator)

        autorRepository.save(autor)

        responseObserver.onNext(
            AutorResponse.newBuilder().setId(autor.id!!).setNome(autor.nome).setEmail(autor.email)
                .setDescricao(autor.descricao).build()
        )
        responseObserver.onCompleted()
    }

}

fun AutorRequest.toAutor(validator: Validator): Autor {
    val autor = Autor(nome = nome, email = email, descricao = descricao)

    val errors = validator.validate(autor)

    if (errors.isNotEmpty()) {
        throw ConstraintViolationException(errors)
    }

    return autor
}