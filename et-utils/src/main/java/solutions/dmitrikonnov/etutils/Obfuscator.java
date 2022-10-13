package solutions.dmitrikonnov.etutils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

@Slf4j
public class Obfuscator {

    public static String obfuscate(Object data) {
        if(data instanceof String){
            String target = (String)data;
            try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte []fingerPrint = digest.digest(target.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encodeHex(fingerPrint));
            }
            catch (Exception e){
                log.error("Hasing failed!");
            }
        }
            log.error("Obfuscation of data-type {} is not implemented. Hashing failed. Returned value: data.toString().",data.getClass().toString());
            return data.toString();

    }
}
