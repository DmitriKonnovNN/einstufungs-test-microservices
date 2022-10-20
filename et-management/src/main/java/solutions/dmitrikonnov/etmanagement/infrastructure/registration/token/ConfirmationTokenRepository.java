package solutions.dmitrikonnov.etmanagement.infrastructure.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ConfirmationTokenRepository extends JpaRepository <ConfirmationToken,Long> {

    Optional<ConfirmationToken>findByToken (String token);

    Optional<ConfirmationToken> findFirstByEtManagementUserIdOrderByIdDesc(Long userId);

    @Transactional
    @Modifying
    @Query ("UPDATE ConfirmationToken c " +
    "SET c.confirmedAt = ?2 " +
    "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query (value = "delete ct from confirmation_token ct "+
            "left join et_management_user u on ct.et_management_user_id=u.id where (u.enabled=true) and (ct.created_at< :createdAt) ", nativeQuery = true)
    void deleteAllWhereUserEnableAfterCreated(LocalDateTime createdAt);

    @Query ("select c from ConfirmationToken as c "+
            " join ETManagementUser as u on c.etManagementUser.id = u.id "+
            "where u.enabled = false " +
            " order by u.id asc, c.expiresAt desc")
    List<ConfirmationToken> findAllTokensOfDisabledUsers();
}
