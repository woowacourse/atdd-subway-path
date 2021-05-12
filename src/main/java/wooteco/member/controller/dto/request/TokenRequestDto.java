package wooteco.member.controller.dto.request;

public class TokenRequestDto {
    private String email;
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
