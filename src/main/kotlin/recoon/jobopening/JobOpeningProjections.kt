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

interface JobOpeningStageCandidatesProjection{
	fun getCandidates() : List<CandidateProjection> 
}

interface CandidateProjection{
		fun getId():Long
		fun getName(): String
}
