package wooteco.auth.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class TokenRequest {
    @Email
    private String email;
    @NotEmpty
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
