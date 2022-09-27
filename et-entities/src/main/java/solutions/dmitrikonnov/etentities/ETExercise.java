package solutions.dmitrikonnov.etentities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import solutions.dmitrikonnov.etenums.ETExerciseFrontEndType;
import solutions.dmitrikonnov.etenums.ETExerciseLevel;
import solutions.dmitrikonnov.etenums.ETExerciseTyp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * this class should be persistent
 * */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table (name = "ET_EXERCISE", indexes = @Index(columnList = "EXERCISE_LEVEL", name = "ET_EXERCISE_LEVEL_IDX"))
public class ETExercise {

    @Id
    @SequenceGenerator(name = "et_exercise_seq",
            sequenceName = "et_exercise_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "et_exercise_seq")
    private Integer exerciseId;

    private String exerciseDefinition;

    /*
    * mappedBy attribute which indicates that the @ManyToOne side is responsible for handling this bidirectional association
    *
    *Vergiss nicht: mappedBy bekommt den Namen, wie das Element im Kind genannt wird, also ggf. im ETItem: "aufgabe"
    *
    * */
    @JsonIgnoreProperties ("exercise")
    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "exercise")
    @Fetch(FetchMode.JOIN)
    private Set<ETItem> items; // Set or List?

    /*
    * https://vladmihalcea.com/jpa-hibernate-synchronize-bidirectional-entity-associations/
    */

    public void addItem(ETItem item) {
        items.add(item);
        item.setExercise(this);
        numberItems++;
    }
    public void removeItem(ETItem item) {
        items.remove(item);
        item.setExercise(null);
        numberItems--;
    }
    /**
     * Either a link to media content or simple text.
     * */

    private short numberItems;
    private String exerciseContent;

    @Enumerated(EnumType.STRING)
    private ETExerciseTyp exerciseType;

    @Column (name = "EXERCISE_LEVEL")
    @Enumerated(EnumType.STRING)
    private ETExerciseLevel exerciseLevel;

    @Enumerated(EnumType.STRING)
    private ETExerciseFrontEndType frontEndType;

    private Integer weigh;
    private Long counter;

    @Temporal(TemporalType.TIMESTAMP)
    @Column (updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date lastModified;


    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column (name = "modified_by")
    private String modifiedBy;


}
