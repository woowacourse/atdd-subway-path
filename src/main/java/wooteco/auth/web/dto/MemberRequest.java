package wooteco.auth.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import wooteco.auth.domain.Member;

public class MemberRequest {
    @Email(message = "이메일 폼을 입력해주세요.")
    private String email;
    @NotEmpty(message = "패스워드를 입력해주세요.")
    private String password;
    @Positive(message = "올바르지 않은 나이 형식입니다.")
    private Integer age;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public Member toMember() {
        return new Member(email, password, age);
    }
}
