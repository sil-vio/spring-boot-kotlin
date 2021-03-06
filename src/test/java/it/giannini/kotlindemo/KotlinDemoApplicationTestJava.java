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

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
        Transaction t1 = new Transaction(null, value, Date.from(Instant.now()));

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


    @Test
    public void flatMap() {
        List<ClasseA> listaA = new ArrayList<>();
        ClasseA classeA = new ClasseA();
        classeA.setName("LISTA_A");
        List<ClasseB> sottoLista = new ArrayList<>();
        ClasseB classeB = new ClasseB();
        String CF = "GNNSLV81P18D612C";
        classeB.setCf(CF);
        sottoLista.add(classeB);
        classeA.setLista(sottoLista);
        listaA.add(classeA);

        String stringa =
                listaA
                        .stream()
                        .flatMap(classeA1 -> classeA1.getLista().stream())
                        .filter(classeB1 -> classeB1.getCf().compareToIgnoreCase(CF) == 0)
                        .map(classeB1 -> new String(classeB1.getCf()))
                        .findAny()
                        .orElse("");

        Assert.assertEquals(stringa, CF);

    }


}

class ClasseA {

    private String name;

    private List<ClasseB> lista;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClasseB> getLista() {
        return lista;
    }

    public void setLista(List<ClasseB> lista) {
        this.lista = lista;
    }
}


class ClasseB {

    private String cf;

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }
}
