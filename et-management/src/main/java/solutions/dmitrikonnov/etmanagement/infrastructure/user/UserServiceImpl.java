package solutions.dmitrikonnov.etmanagement.infrastructure.user;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoGetDetails;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoUpdateRole;

import java.util.List;

@Service ("userServiceImpl")
@Transactional
@AllArgsConstructor (onConstructor_ ={@Autowired})
public class UserServiceImpl implements UserDetailsService, UserService {
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final static String USER_ID_NOT_FOUND_MSG = "user with id %d not found";


    @Value("${app.userServiceImpl.tokenExpirationTime}") public final int DEFAULT_TOKEN_EXPIRATION_MINUTES;
    @Value("${app.userServiceImpl.tokenExpirationTime}") private int TOKEN_EXPIRATION_MINUTES;

    private final UserRepository userRepository;
    private final SignUpUserAndGetToken<String, ETVerwaltungsUser> signUpUserAndGetUUIDTokenImpl;

    @Override
    public boolean checkIfExist(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserDtoGetDetails getDetails(Long id) {
        return userRepository.getUserEntityById(id);
    }

    @Override
    public List<ETVerwaltungsUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void updateUserRole(UserDtoUpdateRole dto) {
        userRepository.updateUserRole(dto.getId(), dto.getRole());
    }

    @Override
    public void deleteById (Long id){
        userRepository.deleteById(id);
    }

    public int getTokenExpiration() {
        return TOKEN_EXPIRATION_MINUTES;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findUserEntityByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    @Override
    public void enableUser (String email){
         userRepository.enableUserEntityByEmail(email);
    }

    @Override
    public void setExpirationTimeOfToken(int minutes) {
        TOKEN_EXPIRATION_MINUTES = minutes;
    }

    @Override
    public int resetExpirationTimeOfToken() {
        return TOKEN_EXPIRATION_MINUTES = DEFAULT_TOKEN_EXPIRATION_MINUTES;
    }

    @Override
    public String signUpUserAndGetToken(ETVerwaltungsUser ETVerwaltungsUser) {
        return signUpUserAndGetUUIDTokenImpl.signUpUserAndGetToken(ETVerwaltungsUser);
    }

}
