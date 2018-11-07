package recoon.common.model

import org.springframework.data.repository.CrudRepository
import java.util.SortedSet
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OrderBy

/*
 IDEAS:
 - Stage duration
 - Minimun approval quorum
*/
@Entity
data class WorkflowStage(
			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			val id:Long = 0,
			val name:String,
			
			@Column(name="stageorder")
			val order:Int
		):Comparable<WorkflowStage> {
	
	override fun compareTo(other: WorkflowStage) = this.order.compareTo(other.order)
};

@Entity
data class Workflow(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		val id:Long = 0,
		val name:String,
		@OneToMany(cascade = [CascadeType.ALL])
		@OrderBy("order")
		val stages:SortedSet<WorkflowStage>
);

interface WorkflowRepository:CrudRepository<Workflow, Long>;