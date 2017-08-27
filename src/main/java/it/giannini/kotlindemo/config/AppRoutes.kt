package it.giannini.kotlindemo.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono


@Configuration
class AppRoutes(val transactionHandler: TransactionHandler) {

    @Bean
    fun apis() = router {
        (accept(APPLICATION_JSON) and "/transaction").nest {
            //
            GET("/{id}", transactionHandler::getById)
            //
            POST("/", transactionHandler::save)
        }

    }
}

@Component
class TransactionHandler(val transactionService: TransactionService) {

    private val log = LoggerFactory.getLogger(TransactionHandler::class.java)


    fun getById(request: ServerRequest): Mono<ServerResponse> =
            request.pathVariable("id")
                    .toMono()
                    .flatMap { id -> ServerResponse.status(HttpStatus.OK).body(fromObject(transactionService.get(id))) }


    fun save(request: ServerRequest): Mono<ServerResponse> = request.bodyToMono(Transaction::class.java).flatMap { t: Transaction? -> ServerResponse.ok().body(fromObject(transactionService.save(t)))}
}


@Service
class TransactionService(val transactionRepository: TransactionRepository) {

    private val log = LoggerFactory.getLogger(TransactionService::class.java)


    fun save(transaction: Transaction?): Mono<Transaction> {
        log.info(transaction.toString())
        return transactionRepository.save(transaction)
    }

    fun get(id: String): Mono<Transaction> {
        return transactionRepository.findById(id)
    }
}

@Repository
interface TransactionRepository : ReactiveMongoRepository<Transaction, String>

data class Transaction(val id: String?, val value: Float)