package wooteco.subway.auth.ui.dto;

import wooteco.subway.auth.ui.dto.valid.StringValidation;

import java.beans.ConstructorProperties;

public class LoginRequest {

    @StringValidation
    private final String email;
    @StringValidation
    private final String password;

    @ConstructorProperties({"email", "password"})
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
