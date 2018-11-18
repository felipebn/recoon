package recoon.jobopening

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.GraphQLResolver
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import recoon.bootstrap.CustomGraphQLContext
import recoon.common.model.JobOpeningCandidateRepository
import recoon.common.model.JobOpeningRepository
import recoon.common.model.WorkflowRepository
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
