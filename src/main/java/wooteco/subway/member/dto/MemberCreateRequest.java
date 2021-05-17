package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

public class MemberCreateRequest {
    private String email;
    private String password;
    private Integer age;

    public MemberCreateRequest() {
    }

    public MemberCreateRequest(String email, String password, Integer age) {
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
