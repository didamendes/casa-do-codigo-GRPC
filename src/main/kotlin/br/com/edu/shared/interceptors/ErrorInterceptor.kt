package br.com.edu.shared.interceptors

import br.com.edu.shared.exception.JaExistente
import br.com.edu.shared.exception.NenhumResultadoEncontrado
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
@InterceptorBean(ErrorAdvice::class)
class ErrorInterceptor: MethodInterceptor<Any, Any> {

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {

        return try {
            context.proceed()
        } catch (e: Exception) {
            val responseObserver = context.parameterValues[1] as StreamObserver<*>

            val status = when(e) {
                is ConstraintViolationException -> Status.INVALID_ARGUMENT.withCause(e).withDescription(e.message)
                is JaExistente -> Status.INVALID_ARGUMENT.withCause(e).withDescription(e.message)
                is NenhumResultadoEncontrado -> Status.NOT_FOUND.withCause(e).withDescription(e.message)
                else -> Status.UNKNOWN.withCause(e).withDescription("um erro inesperado aconteceu")
            }

            val statusRuntimeException = StatusRuntimeException(status)

            responseObserver.onError(statusRuntimeException)
        }

    }


}