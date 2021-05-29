package wooteco.auth.dto;

import lombok.Getter;

@Getter
public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }
}
