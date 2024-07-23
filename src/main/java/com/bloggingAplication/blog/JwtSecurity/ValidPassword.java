package com.bloggingAplication.blog.JwtSecurity;


import org.springframework.stereotype.Component;

@Component
public class ValidPassword {
    public boolean validPassword(String s) {
        if (s.length() < 8 || s.length() >= 20) {
            return false;
        }
        boolean upperCaseLatter = false;
        boolean lowerCaseLatter = false;
        boolean digit = false;
        boolean spCharacter = false;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (Character.isUpperCase(ch)) {
                upperCaseLatter = true;
                // System.out.println(upperCaseLatter);
            }
            if (Character.isLowerCase(ch)) {
                lowerCaseLatter = true;
                // System.out.println(lowerCaseLatter);
            }
            if (Character.isDigit(ch)) {
                digit = true;
                // System.out.println(digit);
            }
            if (!Character.isLetter(ch) && !Character.isDigit(ch) && !Character.isWhitespace(ch)) {
                spCharacter = true;
                // System.out.println(spCharacter);
            }
        }
        return (upperCaseLatter && lowerCaseLatter && digit && spCharacter);
    }
}
