package solutions.dmitrikonnov.etmanagement.infrastructure.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoGetDetails;
import solutions.dmitrikonnov.etmanagement.security.sUtils.UserRole;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface UserRepository extends JpaRepository <ETManagementUser, Long> {

    Optional<ETManagementUser> findUserEntityByEmail (String email);

    List<ETManagementUser> findAllByRoleOrderByRoleAsc(UserRole role);

    boolean existsById (@NotNull Long id);

    @Query (name = "getUserDetails", nativeQuery = true)
    UserDtoGetDetails getUserEntityById(Long id);

    @Transactional
    @Modifying
    @Query ("update ETManagementUser u set u.role = ?2 where u.id =?1")
    void updateUserRole (Long userId, UserRole role);


    @Transactional
    @Modifying
    @Query ("update ETManagementUser u" +
            " set u.firstName = ?2, u.lastName = ?3, u.password = ?4"
            +" where u.email = ?1" )
    void updateUserEntityByEmail (String email, String firstname, String lastname, String password);

    @Transactional
    @Modifying
    @Query ( "update ETManagementUser u" +
            " set u.enabled = true where u.email = ?1"
    )
    void enableUserEntityByEmail(String email);


    @Transactional
    @Modifying
    @Query("update ETManagementUser u set u.locked=?1, u.reasonForLock = ?2 where u.email = ?3")
    void setLockForUserByEmail(boolean lock,String reasonForLock ,String email);



    @Transactional
    @Modifying
    @Query ("delete from ETManagementUser u where u.enabled = false")
    void deleteUserEntities ();

}
