package com.arch.mvc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;

import javax.validation.constraints.NotNull;

/**
 * This DTO represents information that is given by the user
 * in the onboarding/registration process.
 *
 * @author jimil
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationDTO {

    @NotNull
    @JsonProperty(value = "first_name")
    private String fName;

    @NotNull
    @JsonProperty(value = "last_name")
    private String lName;

    @NotNull
    @JsonProperty(value = "username")
    private String username;

    @NotNull
    @JsonProperty(value = "password")
    private String password;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Converts the dto to mongo document type.
     *
     * @return
     */
    public Document toDocument() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(this);
            return Document.parse(json);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
