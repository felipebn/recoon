package recoon.common.model

import org.springframework.data.repository.CrudRepository
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob

enum class ContactType{
	EMAIL, PHONE, ADDRESS
}

@Embeddable
data class Contact(
		@Enumerated(EnumType.STRING)
		val type:ContactType,
		val value:String
)

@Entity
data class Candidate(
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		val id:Long = 0,
		val name:String,
		
		@ElementCollection
		val contacts:MutableList<Contact> = mutableListOf(),
		
		@Lob
		val picture:ByteArray? = null,
		
		@Lob
		val curriculum:ByteArray? = null
);

interface CandidateRepository : CrudRepository<Candidate, Long>;
