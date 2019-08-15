package com.arch.mvc.controllers;

import com.arch.mvc.dto.BaseResponseDTO;
import com.arch.mvc.dto.UserRegistrationDTO;
import com.arch.mvc.exceptions.InvalidOTPException;
import com.arch.mvc.exceptions.PayloadEmptyException;
import com.arch.mvc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller that contains APIs for the login
 * and registration process.
 *
 * @author jimil
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController implements IController {

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
                data.put("username", username);
                baseResponseDTO.setData(data);
            } catch(Exception e) {
                baseResponseDTO.setError(e.getMessage(), e);
            }
        }
        return baseResponseDTO;
    }

    /**
     * This API is used to register a new user to the platform.
     *
     * @param userRegistrationDTO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public BaseResponseDTO signupUser(@RequestBody UserRegistrationDTO userRegistrationDTO) throws Exception {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            userService.registerUser(userRegistrationDTO);
            Map<String, String> data = new HashMap<>();
            data.put("message","User Registered Successfully.");
            baseResponseDTO.setData(data);
        } catch(Exception e) {
            baseResponseDTO.setError(e.getMessage(), e);
        }
        return baseResponseDTO;
    }

    /**
     * This API is used to verify the otp sent to the
     * user's email.
     *
     * @param payload
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/verify_otp", method = RequestMethod.POST)
    public BaseResponseDTO verifyOtp(@RequestBody Map<String, String> payload) throws Exception {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            String username = payload.get("username");
            String otp = payload.get("otp");
            if(userService.verifyOtp(username, otp)) {
                Map<String, String> data = new HashMap<>();
                data.put("message","Email Verified successfully.");
                baseResponseDTO.setData(data);
            } else {
                throw new InvalidOTPException("The OTP you entered was wrong. Please try again.");
            }
        } catch(Exception e) {
            baseResponseDTO.setError(e.getMessage(), e);
        }
        return baseResponseDTO;
    }

    /**
     * This API is used to verify token stored at the front-end.
     *
     * @param payload
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/verify_token", method = RequestMethod.POST)
    public BaseResponseDTO verifyToken(@RequestBody Map<String, String> payload, HttpServletRequest request) throws Exception {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        try {
            String username = getUsername(request);
            Map<String, String> data = new HashMap<>();
            data.put("verified","True");
            baseResponseDTO.setData(data);
        } catch(Exception e) {
            baseResponseDTO.setError(e.getMessage(), e);
        }
        return baseResponseDTO;
    }

}
