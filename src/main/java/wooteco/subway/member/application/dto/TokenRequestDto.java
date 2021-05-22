package wooteco.subway.member.application.dto;

public class TokenRequestDto {

    private final String email;
    private final String password;

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
