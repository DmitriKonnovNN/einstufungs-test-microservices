package solutions.dmitrikonnov.etutils;

/**
 * masks a particular part of data with asterisks
 * */
public class Masker {

    public static String maskEmail(String email) {

        return email.replaceAll("(?<=.{1}).(?=[^@]*?@)", "*");
    }
}
