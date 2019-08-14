package com.arch.mvc.controllers;

import com.arch.mvc.dto.BaseResponseDTO;
import com.arch.mvc.exceptions.PayloadEmptyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/api/user")
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponseDTO login(@RequestBody Map<String, Object> payload) throws Exception {
        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        if(payload.isEmpty()) {
            baseResponseDTO.setError("Username and password cannot be null."
                    , new PayloadEmptyException("Payload cannot be empty."));
        } else {

        }
        return baseResponseDTO;
    }

}
