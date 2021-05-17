package wooteco.subway.auth.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TokenRequest {
    @NotEmpty
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
