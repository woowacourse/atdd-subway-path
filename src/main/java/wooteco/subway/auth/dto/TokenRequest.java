package wooteco.subway.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TokenRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "가입하지 않은 아이디입니다.")
    private String email;
    @NotBlank(message = "패스워드는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}", message = "잘못된 비밀번호입니다.")
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
