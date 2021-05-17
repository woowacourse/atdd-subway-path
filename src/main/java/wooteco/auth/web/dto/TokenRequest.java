package wooteco.auth.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class TokenRequest {
    @Email(message = "이메일 형식을 입력해주세요.")
    private String email;
    @NotEmpty(message = "패스워드를 입력해주세요.")
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
