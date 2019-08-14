package com.arch.mvc.controllers;

import com.arch.mvc.dto.BaseResponseDTO;
import com.arch.mvc.exceptions.PayloadEmptyException;
import com.arch.mvc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller that contains APIs for the login
 * and registration process.
 *
 * @author jimil
 */
@RestController("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * This API is used to attempt login.
     * On successful login, token will be returned to
     * the user that must be used in all subsequent
     * requests.
     *
     * Otherwise, appropriate error messages are
     * returned.
     *
     * @param payload
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponseDTO login(@RequestBody Map<String, Object> payload) throws Exception {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        if(payload.isEmpty()) {
            baseResponseDTO.setError("Username and password cannot be null."
                    , new PayloadEmptyException("Payload cannot be empty."));
        } else {
            try {
                String username = payload.get("username").toString();
                String password = payload.get("password").toString();
                String token = userService.attemptLogin(username, password);
                Map<String, String> data = new HashMap<>();
                data.put("token", token);
                baseResponseDTO.setData(data);
            } catch(Exception e) {
                baseResponseDTO.setError(e.getMessage(), e);
            }
        }
        return baseResponseDTO;
    }

}
