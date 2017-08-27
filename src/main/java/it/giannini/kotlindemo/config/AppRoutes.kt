package it.giannini.kotlindemo.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.bodyFromPublisher
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.net.URI


@Configuration
class AppRoutes(val transactionHandler: TransactionHandler) {

    @Bean
    fun apis() = router {
        (accept(APPLICATION_JSON) and "/transaction").nest {

            GET("/", transactionHandler::getAll)
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
            transactionService.get(request.pathVariable("id")).flatMap { t -> ServerResponse.ok().body(fromObject(t)) }

    fun getAll(request: ServerRequest): Mono<ServerResponse> =
            ServerResponse.ok().body(bodyFromPublisher(transactionService.getAll()))


    fun save(request: ServerRequest): Mono<ServerResponse> =
            request
                    .bodyToMono(Transaction::class.java)
                    .flatMap { t: Transaction? -> transactionService.save(t) }
                    .flatMap { t -> ServerResponse.created(URI.create("/" + t)).body(fromObject(t)) }
}


@Service
class TransactionService(val transactionRepository: TransactionRepository) {

    private val log = LoggerFactory.getLogger(TransactionService::class.java)


    fun save(transaction: Transaction?): Mono<String> {
        log.info(transaction.toString())
        return transactionRepository.save(transaction).map { t -> t?.id }
    }

    fun get(id: String): Mono<Transaction> = transactionRepository.findById(id)


    fun getAll(): Flux<Transaction> = transactionRepository.findAll();


}

@Repository
interface TransactionRepository : ReactiveMongoRepository<Transaction, String>

data class Transaction(val id: String?, val value: Float)