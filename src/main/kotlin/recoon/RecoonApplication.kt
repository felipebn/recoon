package recoon

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class RecoonApplication {
	
}

fun main(args: Array<String>) {
    SpringApplication.run(RecoonApplication::class.java, *args)
}