package solutions.dmitrikonnov.entities;

import lombok.*;
import solutions.dmitrikonnov.ETAufgabenNiveau;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Table(name = "ET_SCHWELLE")
public class ETSchwelle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private ETAufgabenNiveau niveau;
    @Column (nullable = false)
    private Short mindestSchwelle;
    @Column (nullable = false)
    private Short maximumSchwelle;

}
