package solutions.dmitrikonnov.etentities;

import lombok.*;
import solutions.dmitrikonnov.etenums.ETTaskLevel;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Table(name = "ET_LIMIT")
public class ETLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private ETTaskLevel level;
    @Column (nullable = false)
    private Short minLimit;
    @Column (nullable = false)
    private Short maxLimit;

}
