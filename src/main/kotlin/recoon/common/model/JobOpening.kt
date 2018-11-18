package recoon.common.model

import org.springframework.data.repository.CrudRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.CascadeType

@Entity
data class JobOpeningCandidate(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		val id:Long = 0,
		@OneToOne
		val candidate:Candidate,
		@OneToOne
		val currentWorkflowStage:WorkflowStage
)

@Entity
data class JobOpening(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		val id:Long = 0,
		@OneToOne
		val job:Job,
		@OneToOne
		val workflow:Workflow,
		
		@OneToMany(cascade=[CascadeType.ALL])
		val candidates:MutableList<JobOpeningCandidate> = mutableListOf()
);


interface JobOpeningRepository : CrudRepository<JobOpening, Long>{
	fun <T> findJobOpeningsBy(projection:Class<T>) : List<T>
	fun <T> findJobOpeningById(id:Long, projection:Class<T>) : T
	fun <T> findDetailsById(id:Long, projection:Class<T>) : List<T>
	fun <T> findByIdAndCandidates_CurrentWorkflowStage_Id(id:Long, workflowStageId:Long, projection:Class<T>) : T
}

