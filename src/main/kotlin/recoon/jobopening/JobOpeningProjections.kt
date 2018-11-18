package recoon.jobopening

import org.springframework.beans.factory.annotation.Value

interface JobOpeningProjection{
	@Value("#{target.id}")
	fun getId():Long

	@Value("#{target.job.title}")
	fun getName():String
}

interface JobOpeningWorkflowProjection{
	@Value("#{target.workflow.id}")
	fun getId():Long
	@Value("#{target.workflow.name}")
	fun getName():String
}

interface JobOpeningWorkflowStageProjection{
	fun getStages() : List<JobOpeningStageProjection> 
}

interface JobOpeningStageProjection{
	fun getId(): Long
	fun getName(): String
	fun getOrder(): Int
}

interface CandidateProjection{
	@Value("#{target.id}")
	fun getId():Long
	@Value("#{target.candidate.id}")
	fun getCandidateId():String
	@Value("#{target.candidate.name}")
	fun getName():String
}
