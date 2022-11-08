package solutions.dmitrikonnov.etmanagement.infrastructure.user;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoGetDetails;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoUpdateRole;
import solutions.dmitrikonnov.etmanagement.security.sUtils.UserRole;

import java.util.List;
import java.util.Optional;

@Service ("userServiceImpl")
@Transactional
@AllArgsConstructor (onConstructor_ ={@Autowired})
@Slf4j
public class UserServiceImpl implements UserDetailsService, UserService {
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final static String USER_ID_NOT_FOUND_MSG = "user with id %d not found";


    @Value("${app.userService.tokenExpirationTime}") public final int DEFAULT_TOKEN_EXPIRATION_MINUTES;
    @Value("${app.userService.tokenExpirationTime}") private int TOKEN_EXPIRATION_MINUTES;

    private final UserRepository userRepository;
    private final SignUpUserAndGetToken<String, ETManagementUser> signUpUserAndGetUUIDTokenImpl;


    @Override
    public boolean checkIfExist(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public UserDtoGetDetails getDetails(Long id) {
        return userRepository.getUserEntityById(id);
    }

    @Override
    public List<ETManagementUser> findAll() {
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
    public String signUpUserAndGetToken(ETManagementUser ETManagementUser) {
        return signUpUserAndGetUUIDTokenImpl.signUpUserAndGetToken(ETManagementUser);
    }

    @Override
    public void notifyAdminAboutNewUserRequest(ETManagementUser user) {
        Optional.ofNullable(userRepository.findAllByRoleOrderByRoleAsc(UserRole.SUPERADMIN))
                .orElseGet(()->userRepository.findAllByRoleOrderByRoleAsc(UserRole.ROOT))
                .stream()
                .peek(u->log.info("{} was notified about request {}.",u.getFullnameAndRoles(),u))
                .forEach(System.out::println); // should be notify

    }

    @Override
    public Optional<ETManagementUser> findUserByEmail(String email) {
        return userRepository.findUserEntityByEmail(email);
    }

    @Override
    public void addDisabledUserUntilConfirmed(ETManagementUser etManagementUser) {
        userRepository.save(etManagementUser);
    }

    @Override
    public void unlockUser(String reason, String email) {
        userRepository.setLockForUserByEmail(false,reason,email);
    }

    @Override
    public void lockUser(String reason, String email) {
        userRepository.setLockForUserByEmail(true,reason,email);
    }
}
