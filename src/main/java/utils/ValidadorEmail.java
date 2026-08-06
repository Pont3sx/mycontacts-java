package utils;

import java.util.regex.Pattern;

public class ValidadorEmail {
    private static final Pattern PADRAO_EMAIL = Pattern.compile("^[\\w\\-\\.]+@[\\w\\-]+(\\.[\\w\\-]+)*\\.[a-zA-Z]{2,7}$");

    public static boolean emailValido(String email) {
        if (email == null || email.isBlank()){
            return false;
        }
        return PADRAO_EMAIL.matcher(email).matches();
    }
}
