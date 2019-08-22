package com.arch.mvc.services;

import com.arch.mvc.constants.DBConstants;
import com.arch.mvc.dto.UserRegistrationDTO;
import com.arch.mvc.exceptions.IncorrectCredentialsException;
import com.arch.mvc.exceptions.PayloadEmptyException;
import com.arch.mvc.exceptions.UserAlreadyExistsException;
import com.arch.mvc.exceptions.UserDoesNotExistException;
import com.arch.mvc.repositories.UserRepository;
import com.arch.mvc.utils.AppUtils;
import com.arch.mvc.utils.PropertyFileReader;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * Service class for all user operations including
 * login and signup.
 *
 * @author jimil
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Attempts a login and returns a custom token
     * for authentication purposes.
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public String attemptLogin(String username, String password) throws Exception {
        try {
            if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                throw new PayloadEmptyException("Username and password fields cannot be empty");
            }
            Document userDoc = userRepository.findUserByUsername(new Document(DBConstants.USERNAME, username));
            if(userDoc == null) {
                throw new UserDoesNotExistException("Given username does not exist.");
            }
            String token = UUID.randomUUID().toString();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 7);
            Date expiryDate = cal.getTime();
            if(AppUtils.checkPw(password, userDoc.getString(DBConstants.PASSWORD))) {
                Document queryDoc = new Document(DBConstants.USERNAME, username);
                Document updateDoc = new Document(DBConstants.TOKEN, token).append(DBConstants.TOKEN_EXPIRY, expiryDate);
                userRepository.updateUser(queryDoc, updateDoc);
                return token;
            } else {
                throw new IncorrectCredentialsException("Username and password does not match.");
            }
        } catch(PayloadEmptyException e) {
            throw new PayloadEmptyException(e.getMessage());
        } catch(UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        }
    }

    /**
     * Service method to register new user.
     *
     * @param userRegistrationDTO
     * @throws Exception
     */
    public void registerUser(UserRegistrationDTO userRegistrationDTO) throws Exception {
        if(StringUtils.isBlank(userRegistrationDTO.getfName()) || StringUtils.isBlank(userRegistrationDTO.getlName())
                || StringUtils.isBlank(userRegistrationDTO.getUsername()) || StringUtils.isBlank(userRegistrationDTO.getPassword())) {
            throw new PayloadEmptyException("'username', 'password', 'first name', and 'last name' are" +
                    " mandatory fields.");
        }
        Document queryDoc = new Document(DBConstants.USERNAME, userRegistrationDTO.getUsername());
        Document userDoc = userRepository.findUserByUsername(queryDoc);
        if(userDoc != null) {
            throw new UserAlreadyExistsException("The username is already registered.");
        }
        userDoc = userRegistrationDTO.toDocument();
        userDoc.remove(DBConstants.PASSWORD);
        userDoc.append(DBConstants.PASSWORD, AppUtils.hashPassword(userRegistrationDTO.getPassword()));
        String otp = AppUtils.getSaltString();
        sendOtpVerificationMail(userRegistrationDTO.getfName(), userRegistrationDTO.getUsername(), otp);
        userDoc.append(DBConstants.OTP, otp).append(DBConstants.OTP_VERIFIED, false);
        userRepository.insertNewUser(queryDoc, userDoc);
    }

    /**
     * Sends an OTP to verify email.
     * Email is sent via SMTP host with SSL Auth.
     *
     * @param fname
     * @param toEmail
     * @param otp
     * @throws Exception
     */
    public void sendOtpVerificationMail(String fname, String toEmail, String otp) throws Exception {
        String fromEmail = PropertyFileReader.getValue("from.email");
        String fromPwd = PropertyFileReader.getValue("from.pwd");

        Properties props = new Properties();
        props.put("mail.smtp.host", PropertyFileReader.getValue("mail.smtp.host")); //SMTP Host
        props.put("mail.smtp.socketFactory.port", PropertyFileReader.getValue("mail.smtp.socketFactory.port")); //SSL Port
        props.put("mail.smtp.socketFactory.class",
                PropertyFileReader.getValue("mail.smtp.socketFactory.class")); //SSL Factory Class
        props.put("mail.smtp.auth", PropertyFileReader.getValue("mail.smtp.auth")); //Enabling SMTP Authentication
        props.put("mail.smtp.port", PropertyFileReader.getValue("mail.smtp.port")); //SMTP Port

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPwd);
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        System.out.println("Session created");
        String subject = "A.R.J.U.N Email Verification";
        String body = "Hey " + fname + "!\n"
                + "Please verify your email with the below OTP and start learning!\n"
                + "OTP: " + otp;
        AppUtils.sendEmail(session, fromEmail, toEmail,subject, body);
    }

    /**
     * Service method to verify the otp entered by the user.
     *
     * @param username
     * @param otp
     * @return
     * @throws Exception
     */
    public boolean verifyOtp(String username, String otp) throws Exception {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(otp)) {
            throw new PayloadEmptyException("'username' and 'otp' are compulsory fields.");
        }
        Document queryDoc = new Document(DBConstants.USERNAME, username);
        Document userDoc = userRepository.findUserByUsername(queryDoc);
        if(userDoc == null) {
            throw new UserDoesNotExistException("The specified username does not exist.");
        }
        String match_otp = userDoc.getString(DBConstants.OTP);
        if(otp.equals(match_otp)) {
            Document updateDoc = new Document(DBConstants.OTP_VERIFIED, true);
            userRepository.updateUser(queryDoc, updateDoc);
            return true;
        }
        return false;
    }

}
