package wooteco.subway.auth.ui.dto;

import java.beans.ConstructorProperties;

public class LoginRequest {

    private final String email;
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
