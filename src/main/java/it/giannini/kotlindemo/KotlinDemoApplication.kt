package it.giannini.kotlindemo

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication
@EnableReactiveMongoRepositories
class KotlinDemoApplication


fun main(args: Array<String>) {
    SpringApplication.run(KotlinDemoApplication::class.java, *args)
}