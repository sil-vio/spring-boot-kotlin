package it.giannini.kotlindemo.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono


@Configuration
class AppRoutes(val helloHandler: HelloHandler) {

    @Bean
    fun apis() = router {
        (accept(APPLICATION_JSON) and "/hello").nest {
            GET("/single/{name}", helloHandler::byName)
            GET("/multi/{name}?")
        }

    }
}


@Component
class HelloHandler {

    private val log = LoggerFactory.getLogger(HelloHandler::class.java)


    fun byName(request: ServerRequest): Mono<ServerResponse> {

        return request.pathVariable("name").toMono().flatMap { m ->
            ServerResponse.status(HttpStatus.OK).body(fromObject(HelloReponse(m)))
        }
    }
}

data class HelloReponse(val nome: String)