package wooteco.auth.dto;

import javax.validation.constraints.NotNull;

public class TokenRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;

    public TokenRequest() {
    }

    public TokenRequest(String email, String password) {
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
