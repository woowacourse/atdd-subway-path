package wooteco.member.controller.dto.request;

import javax.validation.constraints.NotBlank;

public class SignInRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public SignInRequest() {
    }

    public SignInRequest(String email, String password) {
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
