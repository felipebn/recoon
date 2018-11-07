package recoon

import com.github.javafaker.Faker
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionTemplate
import recoon.common.model.Candidate
import recoon.common.model.CandidateRepository
import recoon.common.model.Contact
import recoon.common.model.ContactType
import recoon.common.model.Job
import recoon.common.model.JobOpening
import recoon.common.model.JobOpeningCandidate
import recoon.common.model.JobOpeningRepository
import recoon.common.model.JobRepository
import recoon.common.model.Workflow
import recoon.common.model.WorkflowRepository
import recoon.common.model.WorkflowStage
import java.util.Random

@SpringBootApplication
@EnableTransactionManagement
class RecoonApplication {
	
}

fun main(args: Array<String>) {
    SpringApplication.run(RecoonApplication::class.java, *args)
}