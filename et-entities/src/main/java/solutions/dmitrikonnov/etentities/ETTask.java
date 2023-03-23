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
import solutions.dmitrikonnov.etenums.ETTaskFrontEndType;
import solutions.dmitrikonnov.etenums.ETTaskLevel;
import solutions.dmitrikonnov.etenums.ETTaskType;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table (name = "ET_TASK", indexes = @Index(columnList = "TASK_LEVEL", name = "ET_TASK_LEVEL_IDX"))
public class ETTask {

    @Id
    @SequenceGenerator(name = "et_task_seq",
            sequenceName = "et_task_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "et_task_seq")
    private Integer taskId;

    private String taskDefinition;

    /*
    * mappedBy attribute which indicates that the @ManyToOne side is responsible for handling this bidirectional association
    *
    *Vergiss nicht: mappedBy bekommt den Namen, wie das Element im Kind genannt wird, also ggf. im ETItem: "aufgabe"
    *
    * */
    @JsonIgnoreProperties ("task")
    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "task")
    @Fetch(FetchMode.JOIN)
    private Set<ETItem> items; // Set or List?

    /*
    * https://vladmihalcea.com/jpa-hibernate-synchronize-bidirectional-entity-associations/
    */

    public void addItem(ETItem item) {
        items.add(item);
        item.setTask(this);
        numberItems++;
    }
    public void removeItem(ETItem item) {
        items.remove(item);
        item.setTask(null);
        numberItems--;
    }
    private short numberItems;

    /**
     * Either a link to media content or simple text.
     * */

    private String taskContent;

    @Enumerated(EnumType.STRING)
    private ETTaskType taskType;

    @Column (name = "TASK_LEVEL")
    @Enumerated(EnumType.STRING)
    private ETTaskLevel taskLevel;

    @Enumerated(EnumType.STRING)
    private ETTaskFrontEndType frontEndType;

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
