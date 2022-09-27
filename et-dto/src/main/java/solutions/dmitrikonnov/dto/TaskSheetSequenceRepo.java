package solutions.dmitrikonnov.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskSheetSequenceRepo extends JpaRepository<TaskSheetPersistedIdSequence,Long> {

}
