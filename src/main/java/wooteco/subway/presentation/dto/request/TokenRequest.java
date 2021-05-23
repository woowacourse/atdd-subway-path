package wooteco.subway.presentation.dto.request;

import javax.validation.constraints.NotEmpty;

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
