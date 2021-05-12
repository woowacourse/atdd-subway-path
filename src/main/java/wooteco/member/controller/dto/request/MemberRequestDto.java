package wooteco.member.controller.dto.request;

import wooteco.member.domain.Member;

public class MemberRequestDto {
    private String email;
    private String password;
    private Integer age;

    public MemberRequestDto() {
    }

    public MemberRequestDto(String email, String password, Integer age) {
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
