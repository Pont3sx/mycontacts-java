package utils;

public class ValidadorEmail {
    public static boolean emailValido(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";

        return email.matches(regex);
    }
}
