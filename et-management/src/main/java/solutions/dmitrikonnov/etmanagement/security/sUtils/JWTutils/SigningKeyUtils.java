package solutions.dmitrikonnov.etmanagement.security.sUtils.JWTutils;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;


public class SigningKeyUtils {

    public static Key formatAndGetSigningKey (String input) {
        byte[] key = Decoders.BASE64.decode(input);
        return Keys.hmacShaKeyFor(key);
    }
}
