package solutions.dmitrikonnov.dto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseSetSequenceRepo extends JpaRepository<ExerciseSetPersistedIdSequence,Long> {

}
