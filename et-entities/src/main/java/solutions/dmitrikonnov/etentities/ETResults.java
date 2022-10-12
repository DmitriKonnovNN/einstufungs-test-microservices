package solutions.dmitrikonnov.etentities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Table (name = "ET_RESULTS")
public class ETResults {


    @Id
    @Column(insertable = true, updatable = false, unique = true)
    private UUID id;

    private Integer taskSheetHash;

    @Column (updatable = false)
    @Enumerated(EnumType.STRING)
    private ETTaskLevel maxReachedLevel = ETTaskLevel.A0;

    @Column(updatable = false)
    private Short numberCorrectAnswers;

    @ElementCollection
    @CollectionTable (name = "et_results_mapping",joinColumns = @JoinColumn (name ="ET_RESULTS_ID"))
    @MapKeyColumn (name = "et_item_id")
    @Column (name = "et_item_correctness")
    private Map<Integer, Boolean> idToCorrectnessMap;

    @ElementCollection
    @CollectionTable (name = "et_level_correct_map", joinColumns = @JoinColumn (name ="ET_RESULTS_ID"))
    @MapKeyColumn (name = "et_level")
    @Column (name = "et_number_correct")
    private Map<String, Short> levelToNumberCorrect;

    @Temporal(TemporalType.TIMESTAMP)
    @Column (updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date createdOn;



}
