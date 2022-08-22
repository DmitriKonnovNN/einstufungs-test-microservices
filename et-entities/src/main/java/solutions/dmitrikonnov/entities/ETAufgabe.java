package solutions.dmitrikonnov.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import solutions.dmitrikonnov.ETAufgabenFrontEndType;
import solutions.dmitrikonnov.ETAufgabenNiveau;
import solutions.dmitrikonnov.ETAufgabenTyp;

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
@Table (name = "ET_AUFGABE", indexes = @Index(columnList = "AUFGABEN_NIVEAU", name = "ET_AUFGABE_NIVEAU_IDX"))
public class ETAufgabe {

    @Id
    @SequenceGenerator(name = "et_aufgabe_seq",
            sequenceName = "et_aufgabe_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "et_aufgabe_seq")
    private Integer aufgabeId;

    private String aufgabenStellung;

    /*
    * mappedBy attribute which indicates that the @ManyToOne side is responsible for handling this bidirectional association
    *
    *Vergiss nicht: mappedBy bekommt den Namen, wie das Element im Kind genannt wird, also ggf. im ETItem: "aufgabe"
    *
    * */
    @JsonIgnoreProperties ("aufgabe")
    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "aufgabe")
    @Fetch(FetchMode.JOIN)
    private Set<ETItem> items; // Set or List?

    /*
    * https://vladmihalcea.com/jpa-hibernate-synchronize-bidirectional-entity-associations/
    */

    public void addItem(ETItem item) {
        items.add(item);
        item.setAufgabe(this);
        zahlItems++;
    }
    public void removeItem(ETItem item) {
        items.remove(item);
        item.setAufgabe(null);
        zahlItems--;
    }
    /**
     * Either a link to media content or simple text.
     * */

    private short zahlItems;
    private String aufgabenInhalt;

    @Enumerated(EnumType.STRING)
    private ETAufgabenTyp aufgabenTyp;

    @Column (name = "AUFGABEN_NIVEAU")
    @Enumerated(EnumType.STRING)
    private ETAufgabenNiveau aufgabenNiveau;

    @Enumerated(EnumType.STRING)
    private ETAufgabenFrontEndType frontEndType;

    private Integer gewichtung;
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
