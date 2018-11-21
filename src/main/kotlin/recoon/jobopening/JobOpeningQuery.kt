package recoon.jobopening

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import org.apache.commons.lang3.RandomUtils
import org.springframework.stereotype.Component
import recoon.bootstrap.CustomGraphQLContext
import recoon.common.model.JobOpeningCandidateRepository
import recoon.common.model.JobOpeningRepository
import recoon.common.model.WorkflowRepository
import recoon.jobopening.CandidateProjection
import recoon.jobopening.JobOpeningProjection
import recoon.jobopening.JobOpeningStageProjection
import recoon.jobopening.JobOpeningWorkflowProjection
import recoon.jobopening.JobOpeningWorkflowStageProjection
import java.util.Random
import javax.transaction.Transactional

@Component
class JobOpeningQuery(
		val jobOpeningRepository: JobOpeningRepository,
		val jobOpeningCandidateRepository: JobOpeningCandidateRepository
): GraphQLQueryResolver{
	
	fun jobOpenings(): List<JobOpeningProjection>{
		return jobOpeningRepository.findJobOpeningsBy(JobOpeningProjection::class.java)
	}
    
    fun jobOpening(id: Long): JobOpeningProjection{
		return jobOpeningRepository.findJobOpeningById(id, JobOpeningProjection::class.java)
	}
	
	fun jobOpeningCandidate(id:Long): CandidateProjection{
		return jobOpeningCandidateRepository.findJobOpeningCandidateById(id, CandidateProjection::class.java)
	}
}

@Component
class JobOpeningProjectionResolver(
		val jobOpeningRepository: JobOpeningRepository
): GraphQLResolver<JobOpeningProjection>{

	fun getWorkflow(jobOpening:JobOpeningProjection, fetchEnv:DataFetchingEnvironment) : JobOpeningWorkflowProjection{
		val jobOpeningData : JobOpeningProjection = fetchEnv.getSource()
		fetchEnv.setCurrentJobOpeningId(jobOpeningData.getId())
		return jobOpeningRepository.findJobOpeningById(jobOpeningData.getId(), JobOpeningWorkflowProjection::class.java)
	} 

	fun getApplicantCount(jobOpening:JobOpeningProjection) : Long{
		//TODO implement as query
		return RandomUtils.nextLong(0, 100)
	}
	
	fun getResponsible(jobOpening:JobOpeningProjection) : String{
		//TODO implement as query
		val names = listOf("Leela Welch",
						"Jameel Glover",
						"Boyd Corrigan",
						"Rodney Meyers",
						"Bradleigh Hodge",
						"Kingston Acosta",
						"Zeshan Regan",
						"Kasey Samuels",
						"Chloe Atkinson",
						"Ava-Grace Nava")
		return names.shuffled(Random(jobOpening.getId())).first()
	}	
}

@Component
@Transactional
class JobOpeningWorkflowProjectionResolver(
		val workflowRepository: WorkflowRepository
): GraphQLResolver<JobOpeningWorkflowProjection>{

	fun getStages(workflowData:JobOpeningWorkflowProjection, fetchEnv:DataFetchingEnvironment) : List<JobOpeningStageProjection>{
		return workflowRepository.findWorkflowById(workflowData.getId(), JobOpeningWorkflowStageProjection::class.java).getStages()
	} 
		
}

@Component
class JobOpeningStageProjectionResolver(
		val jobOpeningCandidateRepository: JobOpeningCandidateRepository
): GraphQLResolver<JobOpeningStageProjection>{

	fun getCandidates(stage:JobOpeningStageProjection, fetchEnv:DataFetchingEnvironment) : List<CandidateProjection>{
		val jopOpeningId : Long = fetchEnv.getCurrentJobOpeningId()
		return jobOpeningCandidateRepository.findJobOpeningCandidateByJobOpeningIdAndCurrentWorkflowStageId(jopOpeningId, stage.getId(), CandidateProjection::class.java)
	} 
		
}

fun DataFetchingEnvironment.setCurrentJobOpeningId(value:Long){
	(this.getContext() as CustomGraphQLContext).addResolvedValue("JOB_OPENING_ID", value)
}

fun DataFetchingEnvironment.getCurrentJobOpeningId() : Long{
	return (this.getContext() as CustomGraphQLContext).getResolvedValue("JOB_OPENING_ID") as Long
}
