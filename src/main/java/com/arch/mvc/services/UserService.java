package com.arch.mvc.services;

import com.arch.mvc.exceptions.PayloadEmptyException;
import com.arch.mvc.exceptions.UserAlreadyExistsException;
import com.arch.mvc.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public String attemptLogin(String username, String password) throws Exception {
        try {
            if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                throw new PayloadEmptyException("Username and password fields cannot be empty");
            }
            password = bCryptPasswordEncoder.encode(password);
            // TODO: Generate user token and add to insertDoc being passed below
            if(userRepository.insertUser("username"
                    , new Document("username", username).append("password", password))) {
                // TODO: Return custom token
            } else {
                throw new UserAlreadyExistsException("User already exists.");
            }
        } catch(PayloadEmptyException e) {
            throw new PayloadEmptyException(e.getMessage());
        } catch(UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException(e.getMessage());
        }
        return null;
    }

}
