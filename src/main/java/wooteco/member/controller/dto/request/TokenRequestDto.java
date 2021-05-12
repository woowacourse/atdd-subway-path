package wooteco.member.controller.dto.request;

import javax.validation.constraints.NotBlank;

public class TokenRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public TokenRequestDto() {
    }

    public TokenRequestDto(String email, String password) {
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
