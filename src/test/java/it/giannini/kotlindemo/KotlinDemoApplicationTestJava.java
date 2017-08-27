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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = it.giannini.kotlindemo.KotlinDemoApplication.class)
public class KotlinDemoApplicationTestJava {

	@Autowired
	ApplicationContext context;


	WebTestClient client;

	@Before
	public void setup() {
		client = WebTestClient
				.bindToServer()
				//.webFilter(mutator)
				//.configureClient()
				.baseUrl("http://localhost:8080/")
				.build();
	}

	@Test
	public void postEntity() {

		Transaction t =  new Transaction(null, 234.5F);

		client.post()
                .uri("transaction/")
                .body(BodyInserters.fromObject(t))
                .exchange()
                .expectStatus().isOk(); //.expectBody(Transaction.class).returnResult().getResponseBody();

		Assert.assertNotNull(t.getId());

		System.out.println(client.get().uri("transaction/" + t.getId()).exchange()
				.expectStatus().isOk().expectBody(Transaction.class).returnResult().getResponseBody());

	}

}
