package solutions.dmitrikonnov.etmanagement.security.sUtils.JWTutils.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

    private final String jwt;


}
