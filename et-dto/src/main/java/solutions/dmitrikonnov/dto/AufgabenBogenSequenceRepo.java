package solutions.dmitrikonnov.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AufgabenBogenSequenceRepo extends JpaRepository<AufgabenBogenPersistedIdSequence,Long> {

}
