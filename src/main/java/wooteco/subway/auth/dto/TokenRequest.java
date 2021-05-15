package wooteco.subway.auth.dto;

import wooteco.subway.member.domain.Member;

public class TokenRequest {

    private String email;
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

    public Member toEntity() {
        return new Member(email, password);
    }
}
