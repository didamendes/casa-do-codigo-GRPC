package br.com.edu.usuario

import br.com.edu.UsuarioGrpcServiceGrpc
import br.com.edu.UsuarioRequest
import br.com.edu.UsuarioResponse
import br.com.edu.shared.exception.JaExistente
import br.com.edu.shared.interceptors.ErrorAdvice
import br.com.edu.usuario.model.Usuario
import br.com.edu.usuario.model.UsuarioRepository
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.validation.ConstraintViolationException
import javax.validation.Validator

@Singleton
@ErrorAdvice
class UsuarioEndpoint(val validator: Validator, val usuarioRepository: UsuarioRepository) :
    UsuarioGrpcServiceGrpc.UsuarioGrpcServiceImplBase() {

    override fun cadastrar(request: UsuarioRequest, responseObserver: StreamObserver<UsuarioResponse>) {
        val usuario = request.toUsuario(validator)

        if (usuarioRepository.existsByLogin(usuario.login)) {
            throw JaExistente("Email já existente")
        }

        usuarioRepository.save(usuario)

        responseObserver.onNext(
            UsuarioResponse.newBuilder().setId(usuario.id!!).setLogin(usuario.login)
                .setDataCriacao(usuario.dataCriacao.toString()).build()
        )
        responseObserver.onCompleted()
    }

}

fun UsuarioRequest.toUsuario(validator: Validator): Usuario {
    val usuario = Usuario(login = login, senha = senha)

    val erros = validator.validate(usuario)

    if (erros.isNotEmpty()) {
        throw ConstraintViolationException(erros)
    }

    return usuario
}