package wooteco.subway.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class TokenRequest {

    @Email(message = "Email 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
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
