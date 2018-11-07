package recoon.common.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import org.springframework.data.repository.CrudRepository

@Entity
data class Job(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		val id:Long = 0,
		
		val title:String,
		val description:String = ""
);

interface JobRepository:CrudRepository<Job, Long>;