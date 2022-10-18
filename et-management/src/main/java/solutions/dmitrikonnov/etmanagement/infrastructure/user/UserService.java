package solutions.dmitrikonnov.etmanagement.infrastructure.user;


import solutions.dmitrikonnov.etmanagement.infrastructure.registration.RegistrationRequest;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoGetDetails;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoUpdateRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void deleteById (Long id);
    void enableUser (String email);
    String signUpUserAndGetToken(ETManagementUser eTManagementUser);
    List<ETManagementUser> findAll ();
    int getTokenExpiration();
    void updateUserRole(UserDtoUpdateRole dto);
    boolean checkIfExist(Long id);
    UserDtoGetDetails getDetails (Long id);
    void setExpirationTimeOfToken (int minutes);
    int resetExpirationTimeOfToken ();
    void notifyAdminAboutNewUserRequest(ETManagementUser etManagementUser);
    Optional<ETManagementUser> findUserByEmail(String email);
    void addDisabledUserUntilConfirmed (ETManagementUser etManagementUser);
    void lockUser(String reason, String email);
    void unlockUser(String reason, String email);
}
