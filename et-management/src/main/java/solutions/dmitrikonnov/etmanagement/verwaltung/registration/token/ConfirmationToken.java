package solutions.dmitrikonnov.etmanagement.verwaltung.registration.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import solutions.dmitrikonnov.etmanagement.verwaltung.user.ETVerwaltungsUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @SequenceGenerator(name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence")
    private Long id;
    @Column (nullable = false)
    private String token;
    @Column (nullable = false)
    private LocalDateTime createdAt;
    @Column (nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (nullable = false, name = "et_verwaltungs_user_id")
    private ETVerwaltungsUser etVerwaltungsUser;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiredAt,
                             ETVerwaltungsUser etVerwaltungsUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiredAt;
        this.etVerwaltungsUser = etVerwaltungsUser;
    }
}
