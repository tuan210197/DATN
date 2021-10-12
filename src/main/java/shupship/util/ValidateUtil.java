package shupship.util;

import java.util.regex.Pattern;

public class ValidateUtil {

    public static boolean regexValidation(String input, String stringPattern) {
        Pattern pattern = Pattern.compile(stringPattern);
        return pattern.matcher(input).matches();
    }
}
