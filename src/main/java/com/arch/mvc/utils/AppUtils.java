package com.arch.mvc.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Utility class that has methods used app-wide
 *
 * @author jimil
 */
public class AppUtils {

    /**
     * Util method to check whether plaintext password
     * matches hashed password found in the database.
     *
     * @param plainPassword
     * @param hashedPassword
     * @return
     */
    public static boolean checkPw(String plainPassword, String hashedPassword) {
        if(BCrypt.checkpw(plainPassword, hashedPassword)) {
            return true;
        }
        return false;
    }

}
