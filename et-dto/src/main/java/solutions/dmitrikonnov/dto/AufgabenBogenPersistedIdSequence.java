package solutions.dmitrikonnov.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * This is a cludgy entity whose existing goal is merely to create a sequence without additional
 * SQL-scripts within data base initialization. In fact, the lone field down below isn't even mutated. Instead we directly
 * make use of the belonging sequence itself.
 * */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AufgabenBogenPersistedIdSequence {
    @Id
    @SequenceGenerator(name = "et_aufgabenbogen_seq",
            sequenceName = "et_aufgabenbogen_seq",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "et_aufgabenbogen_seq")
    private Long aufgabenBogenId;
}
