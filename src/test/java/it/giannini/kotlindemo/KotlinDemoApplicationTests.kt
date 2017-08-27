package it.giannini.kotlindemo

import it.giannini.kotlindemo.config.Transaction
import it.giannini.kotlindemo.config.TransactionHandler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient


@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(KotlinDemoApplication::class))
class KotlinDemoApplicationTests {

    private val log = LoggerFactory.getLogger(TransactionHandler::class.java)

    lateinit var client: WebTestClient

    @Before
    fun setup() {
        client = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:8080/")
                .build()
    }

    @Test
    fun quandoChiamatoTornaPippo_V2() {


        val t = Transaction(null,123.5F)

//
//        client.post()
//                .uri("transaction/")
//                .body(BodyInserters.fromObject(t))
//                .exchange()
//                .expectStatus().isOk().expectBody().jsonPath("value").isEqualTo(123.5F)

//        Assert.assertNotNull(result.responseBody)
//        Assert.assertNotNull(result.responseBody.)
//
//        result = client.get()
//                .uri("transaction/" + result.responseBody.id)
//                .exchange()
//                .expectStatus().isOk()
//                .expectBody(Transaction::class.java).returnResult()
//
//        Assert.assertEquals(t.value, result.responseBody.value)


    }
}