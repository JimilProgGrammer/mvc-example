package com.arch.mvc.services;

import com.arch.mvc.exceptions.IncorrectCredentialsException;
import com.arch.mvc.exceptions.PayloadEmptyException;
import com.arch.mvc.exceptions.UserAlreadyExistsException;
import com.arch.mvc.exceptions.UserDoesNotExistException;
import com.arch.mvc.repositories.UserRepository;
import com.arch.mvc.utils.AppUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
            Document userDoc = userRepository.findUserByUsername(new Document("username", username));
            if(userDoc == null) {
                throw new UserDoesNotExistException("Given username does not exist.");
            }
            String token = UUID.randomUUID().toString();
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, 7);
            Date expiryDate = cal.getTime();
            if(AppUtils.checkPw(password, userDoc.getString("password"))) {
                Document queryDoc = new Document("username", username);
                Document updateDoc = new Document("token", token).append("token_expiry", expiryDate);
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

}
