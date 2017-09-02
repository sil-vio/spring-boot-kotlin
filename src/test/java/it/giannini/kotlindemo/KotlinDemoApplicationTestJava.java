package it.giannini.kotlindemo;

import it.giannini.kotlindemo.config.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = it.giannini.kotlindemo.KotlinDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KotlinDemoApplicationTestJava {

    @Autowired
    ApplicationContext context;


    WebTestClient client;

    @Before
    public void setup() {
        client = WebTestClient
                .bindToApplicationContext(context)
                //.bindToServer()
                //.webFilter(mutator)
                .configureClient()
                .baseUrl("http://localhost:8080/")
                .build();
    }

    @Test
    public void postEntity() {


        float value = 234.5F;
        Transaction t1 = new Transaction(null, value);

        client.post()
                .uri("transaction/")
                .body(BodyInserters.fromObject(t1))
                .exchange()
                .expectStatus().isCreated();

		client.get()
                .uri("events/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Transaction.class).hasSize(1);

        client.get()
                .uri("transaction/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Transaction.class).hasSize(1)
                .consumeWith(transactionList -> {
                    transactionList.getResponseBody()
                            .stream()
                            .forEach(transaction -> Assert.assertEquals(value, transaction.getValue(), 0F));
                });

    }

}
