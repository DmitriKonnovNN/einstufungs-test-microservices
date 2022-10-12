package solutions.dmitrikonnov.etmanagement.infrastructure.user;


import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoGetDetails;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoUpdateRole;

import java.util.List;

public interface UserService {
    public void deleteById (Long id);
    public void enableUser (String email);
    public String signUpUserAndGetToken(ETVerwaltungsUser ETVerwaltungsUser);
    public List<ETVerwaltungsUser> findAll ();
    public int getTokenExpiration();
    void updateUserRole(UserDtoUpdateRole dto);
    boolean checkIfExist(Long id);
    public UserDtoGetDetails getDetails (Long id);
    public void setExpirationTimeOfToken (int minutes);
    public int resetExpirationTimeOfToken ();

}
