package com.arch.mvc.controllers;

import com.arch.mvc.exceptions.InvalidTokenException;
import com.arch.mvc.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Controller interface , every controller in the application should
 * implement this interface , contains basic operation functions.
 *
 * @author jimil
 */
public interface IController {

    /**
     * Default method that is used to fetch username based on the token
     * received from the header that was sent along with the request.
     *
     * @param request
     * @return
     * @throws Exception
     */
    public default String getUsername(HttpServletRequest request) throws Exception {
        String token = request.getHeader("x-auth-token");
        if(StringUtils.isBlank(token) || !token.contains("BEARER")) {
            throw new InvalidTokenException("Invalid Token format.");
        }
        token = StringUtils.removeStart(token, "BEARER").trim();
        Document userDoc = new UserRepository().findUserByToken(new Document("token", token));
        if(userDoc == null) {
            throw new InvalidTokenException("Invalid token.");
        }
        Date expiryDate = userDoc.getDate("token_expiry");
        if(new Date().before(expiryDate)) {
            return userDoc.getString("username");
        }
        throw new InvalidTokenException("Token expired.");
    }

}
