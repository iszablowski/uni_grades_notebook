package application;

public class InputValidation {
    public static boolean isValidString(String string) {
        return !(string == null || string.trim().isEmpty());
    }
}
