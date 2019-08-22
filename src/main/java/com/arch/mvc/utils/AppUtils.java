package com.arch.mvc.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Random;

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

    /**
     * Util method to hash password using BCrypt
     *
     * @param plainTextPassword
     * @return
     */
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * This method is used to return a random 6-digit OTP
     *
     * @return
     */
    public static int generateRandomNumberSixDigit() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000;
        return number ;
    }

    /**
     * Generates a random alpha-numeric string of length 6
     * that is used as OTP.
     *
     * @return
     */
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    /**
     * Utility method to send simple HTML email
     *
     * @param session
     * @param fromEmail
     * @param toEmail
     * @param subject
     * @param body
     */
    public static void sendEmail(Session session, String fromEmail, String toEmail, String subject, String body){
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(fromEmail, "A.R.J.U.N Verification"));
            msg.setReplyTo(InternetAddress.parse(fromEmail, false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);
            System.out.println("EMail Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
