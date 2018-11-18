package recoon.bootstrap

import com.github.javafaker.Faker
import org.slf4j.Logger
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.transaction.PlatformTransactionManager
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
import org.slf4j.LoggerFactory



@Configuration
class DemoDataBootstrap{
	companion object {
		val log = LoggerFactory.getLogger(this::class.java.name.substringBefore("\$Companion"))
	}
	
	@Bean("workfowCreator")
	fun createWorkflows(workflowRepository:WorkflowRepository) = CommandLineRunner(){
		workflowRepository.save(Workflow(
				name="Non Technical Workflow",
				stages=sortedSetOf(
						WorkflowStage(name="Applied", order=0),
						WorkflowStage(name="Phone Screen", order=1),
						WorkflowStage(name="Main Interview", order=2),
						WorkflowStage(name="Decision Interview", order=3),
						WorkflowStage(name="Offer", order=4)
				)
		))
		
		workflowRepository.save(Workflow(
				name="Technical Workflow",
				stages=sortedSetOf(
						WorkflowStage(name="Applied", order=0),
						WorkflowStage(name="Phone Screen", order=1),
						WorkflowStage(name="Coding Test", order=2),
						WorkflowStage(name="Main Interview", order=3),
						WorkflowStage(name="Decision Interview", order=4),
						WorkflowStage(name="Offer", order=5)
				)
		))
		
		log.info("Created workflows")
	}
	
	@Bean("jobCreator")
	fun createJobs(jobRepository:JobRepository) = CommandLineRunner(){
		val faker = Faker()
		for(i in 0 until 5){
			val newJob = jobRepository.save(Job(
				title=faker.job().title(),
				description=faker.lorem().sentence(3,10)
			))
			log.info("Created job {}", newJob)
		}
	}
	
	@Bean("jobOpeningCreator")
	@DependsOn("workfowCreator","jobCreator")
	fun createJobOpenings(jobRepository:JobRepository, workflowRepository:WorkflowRepository, jobOpeningRepository:JobOpeningRepository) = CommandLineRunner(){
		val jobs = jobRepository.findAll()
		val workflows = workflowRepository.findAll()
		
		val jobsPerWorkflow = jobs.chunked(Math.ceil(jobs.count()/workflows.count().toDouble()).toInt())
		
		workflows.forEachIndexed{ i, w ->
			jobsPerWorkflow.get(i).forEach{ j ->
				jobOpeningRepository.save(JobOpening(job=j, workflow=w))
				log.info("Created job opening for '{}' with workflow '{}'", j.title, w.name)
			}
		}
	}
	
	@Bean("candidatesCreator")
	@DependsOn("jobOpeningCreator")
	fun createCandidates(
			transactionManager:PlatformTransactionManager, 
			jobOpeningRepository:JobOpeningRepository,
			candidateRepository:CandidateRepository) = CommandLineRunner(){
		val tx = TransactionTemplate(transactionManager) 
		tx.execute{
			val openings = jobOpeningRepository.findAll()
					openings.drop(1).forEachIndexed{ i, opening ->
					val minCandidates = i + 1
					val maxCandidates = minCandidates + Random().nextInt(minCandidates + 6)
					for(c in minCandidates..maxCandidates){
						val newCandidate = candidateRepository.save(createFakeCandidate())
								opening.candidates.add(JobOpeningCandidate(
										candidate=newCandidate,
										currentWorkflowStage=opening.workflow.stages.first(),
										jobOpening=opening
								))
								log.info("New candidate '{}' for '{}'", newCandidate, opening.job.title)
					}
					jobOpeningRepository.save(opening)
			}
		}
	}
	
	fun createFakeCandidate():Candidate{
		val faker = Faker()
		val numberOfContacts = Random().nextInt(ContactType.values().size + 1)
		val fakePerson = faker.name()
		
		val candidateContacts = ContactType.values().asList().shuffled()
				.take(numberOfContacts)
				.map { when(it){
					ContactType.EMAIL 	-> 	Contact(type=it, value=faker.internet().emailAddress(fakePerson.username()))
					ContactType.PHONE 	-> 	Contact(type=it, value=faker.phoneNumber().cellPhone())
					ContactType.ADDRESS -> 	Contact(type=it, value=faker.address().fullAddress())
				}}
				.toMutableList()

		return Candidate(
				name=fakePerson.fullName(),
				contacts=candidateContacts
		)
	}
}