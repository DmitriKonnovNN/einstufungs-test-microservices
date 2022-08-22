package solutions.dmitrikonnov.etverwaltung.verwaltung.user;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.etverwaltung.verwaltung.registration.token.ConfirmationToken;
import solutions.dmitrikonnov.etverwaltung.verwaltung.registration.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Service
@AllArgsConstructor(onConstructor_ ={@Autowired})
public class SignUpUserAndGetUUIDTokenImpl implements SignUpUserAndGetToken <String, ETVerwaltungsUser>{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Value("${app.userServiceImpl.tokenExpirationTime}") private int TOKEN_EXPIRATION_MINUTES;
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
     * @param ETVerwaltungsUser UserEntity to be persisted
     * @return a UUID-token
     * @throws IllegalStateException if email is occupied, which means user is enabled, or if the previous token
     * hasn't expired yet
     *
     *
     * */

    @Override
    public String signUpUserAndGetToken(ETVerwaltungsUser ETVerwaltungsUser) {
        Set<String> tokens = new HashSet<>();

        userRepository.findUserEntityByEmail(ETVerwaltungsUser.getEmail())
                .ifPresentOrElse((persistedUser)-> {
                    if(persistedUser.isEnabled()){
                        throw new IllegalStateException(String.format(EMAIL_ALREADY_OCCUPIED_MSG, ETVerwaltungsUser.getEmail()));}
                    if(!persistedUser.isEnabled()){
                        tokens.addAll(getNewTokenIfOlderExpired(ETVerwaltungsUser,persistedUser));}

                },()-> tokens.addAll(getTokenForNotPersistedUser(ETVerwaltungsUser)));
        return tokens.iterator().next();
    }

    private Set<String> getTokenForNotPersistedUser(ETVerwaltungsUser ETVerwaltungsUser){
        ETVerwaltungsUser.setPassword(bCryptPasswordEncoder.encode(ETVerwaltungsUser.getPassword()));
        userRepository.save(ETVerwaltungsUser);
        return Collections.singleton(generateToken(ETVerwaltungsUser));
    }

    private Set<String> getNewTokenForPersistedUser (ETVerwaltungsUser ETVerwaltungsUser, ETVerwaltungsUser persistedUser) {
        ETVerwaltungsUser.setPassword(bCryptPasswordEncoder.encode(ETVerwaltungsUser.getPassword()));
        userRepository.updateUserEntityByEmail(
                ETVerwaltungsUser.getEmail(),
                ETVerwaltungsUser.getFirstName(),
                ETVerwaltungsUser.getLastName(),
                ETVerwaltungsUser.getPassword());
        return Collections.singleton(generateToken(persistedUser));

    }

    private Set<String> getNewTokenIfOlderExpired(ETVerwaltungsUser ETVerwaltungsUser, ETVerwaltungsUser persistedUser) {
        if(confirmationTokenService.findTokenByUserId(persistedUser.getId()).isPresent()) {
            if (isTokenNotExpired(persistedUser)) throw new IllegalStateException(TOKEN_HAS_NOT_EXPIRED_MSG);

        }
        return getNewTokenForPersistedUser(ETVerwaltungsUser,persistedUser);

    }

    private boolean isTokenNotExpired(ETVerwaltungsUser persistedUser) {

        if(confirmationTokenService
                .findTokenByUserId(persistedUser.getId())
                .get()
                .getExpiresAt()
                .isAfter(LocalDateTime.now())){
            return true; }
        return false;
    }



    private String generateToken(ETVerwaltungsUser ETVerwaltungsUser){

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(TOKEN_EXPIRATION_MINUTES),
                ETVerwaltungsUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }
}
