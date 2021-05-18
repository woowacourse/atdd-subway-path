package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MemberUpdateRequest {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotNull
    @Min(1)
    private Integer age;

    public MemberUpdateRequest() {
    }

    public MemberUpdateRequest(String email, String password, Integer age) {
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
        return new Member(email, null, age);
    }
}
