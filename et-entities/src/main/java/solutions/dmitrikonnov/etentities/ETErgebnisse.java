package solutions.dmitrikonnov.etentities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import solutions.dmitrikonnov.etenums.ETAufgabenNiveau;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Table (name = "ET_ERGEBNISSE")
public class ETErgebnisse {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",
            strategy = "org.hibernate.id.UUIDGenerator")
    private  String id;

    private Integer aufgabenBogenHash;

    @Column (updatable = false)
    @Enumerated(EnumType.STRING)
    private ETAufgabenNiveau maxErreichtesNiveau = ETAufgabenNiveau.A0;

    @Column(updatable = false)
    private Short zahlRichtigerAntworten;

    @ElementCollection
    @CollectionTable (name = "et_ergebnisse_mapping",joinColumns = @JoinColumn (name ="ET_ERGEBNISSE_ID"))
    @MapKeyColumn (name = "et_item_id")
    @Column (name = "et_item_correctness")
    private Map<Integer, Boolean> idZuRichtigkeitMap;

    @ElementCollection
    @CollectionTable (name = "et_niveau_richtige_map", joinColumns = @JoinColumn (name ="ET_ERGEBNISSE_ID"))
    @MapKeyColumn (name = "et_niveau")
    @Column (name = "et_zahl_richtiger")
    private Map<String, Short> niveauZurZahlRichtiger;

    @Temporal(TemporalType.TIMESTAMP)
    @Column (updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Date createdOn;



}
