package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

public class MemberUpdateRequest {
    private String email;
    private Integer age;

    public MemberUpdateRequest() {
    }

    public MemberUpdateRequest(String email, Integer age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public Member toMember() {
        return new Member(email, null, age);
    }
}
