package solutions.dmitrikonnov.etmanagement.infrastructure.user;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.etmanagement.infrastructure.registration.token.ConfirmationToken;
import solutions.dmitrikonnov.etmanagement.infrastructure.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Service
@AllArgsConstructor(onConstructor_ ={@Autowired})
public class SignUpUserAndGetUUIDTokenImpl implements SignUpUserAndGetToken <String, ETManagementUser>{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Value("${app.userService.tokenExpirationTime}") private int TOKEN_EXPIRATION_MINUTES;
    private final static String TOKEN_HAS_NOT_EXPIRED_MSG = "token hasn't expired yet";
    private final static String EMAIL_ALREADY_OCCUPIED_MSG = "email %s already occupied";

    /**
     * If email {
     * is occupied (==user is enabled), throw Exception.
     * If a token for particular email has been generated yet, check whether it's expired.
     * If not expired, throw Exception.
     * If expired, generate a new token and update first name, last name and password.
     * }
     *
     * If email ain't occupied, persist it and generate a token for signing up.
     *
     * @param ETManagementUser UserEntity to be persisted
     * @return a UUID-token
     * @throws IllegalStateException if email is occupied, which means user is enabled, or if the previous token
     * hasn't expired yet
     *
     *
     * */

    @Override
    public String signUpUserAndGetToken(ETManagementUser ETManagementUser) {
        Set<String> tokens = new HashSet<>();

        userRepository.findUserEntityByEmail(ETManagementUser.getEmail())
                .ifPresentOrElse((persistedUser)-> {
                    if(persistedUser.isEnabled()){
                        throw new IllegalStateException(String.format(EMAIL_ALREADY_OCCUPIED_MSG, ETManagementUser.getEmail()));}
                    if(!persistedUser.isEnabled()){
                        tokens.addAll(getNewTokenIfOlderExpired(ETManagementUser,persistedUser));}

                },()-> tokens.addAll(getTokenForNotPersistedUser(ETManagementUser)));
        return tokens.iterator().next();
    }

    private Set<String> getTokenForNotPersistedUser(ETManagementUser ETManagementUser){
        ETManagementUser.setPassword(bCryptPasswordEncoder.encode(ETManagementUser.getPassword()));
        userRepository.save(ETManagementUser);
        return Collections.singleton(generateToken(ETManagementUser));
    }

    private Set<String> getNewTokenForPersistedUser (ETManagementUser ETManagementUser, ETManagementUser persistedUser) {
        ETManagementUser.setPassword(bCryptPasswordEncoder.encode(ETManagementUser.getPassword()));
        userRepository.updateUserEntityByEmail(
                ETManagementUser.getEmail(),
                ETManagementUser.getFirstName(),
                ETManagementUser.getLastName(),
                ETManagementUser.getPassword());
        return Collections.singleton(generateToken(persistedUser));

    }

    private Set<String> getNewTokenIfOlderExpired(ETManagementUser ETManagementUser, ETManagementUser persistedUser) {
        if(confirmationTokenService.findTokenByUserId(persistedUser.getId()).isPresent()) {
            if (isTokenNotExpired(persistedUser)) throw new IllegalStateException(TOKEN_HAS_NOT_EXPIRED_MSG);

        }
        return getNewTokenForPersistedUser(ETManagementUser,persistedUser);

    }

    private boolean isTokenNotExpired(ETManagementUser persistedUser) {

        if(confirmationTokenService
                .findTokenByUserId(persistedUser.getId())
                .get()
                .getExpiresAt()
                .isAfter(LocalDateTime.now())){
            return true; }
        return false;
    }



    private String generateToken(ETManagementUser ETManagementUser){

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES),
                ETManagementUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
}
