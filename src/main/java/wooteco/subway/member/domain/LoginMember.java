package wooteco.subway.member.domain;

import wooteco.subway.member.domain.Member;

public class LoginMember {

    private final Member member;

    public LoginMember(Member member) {
        this.member = member;
    }

    public Long getId() {
        return member.getId();
    }

    public String getEmail() {
        return member.getEmail();
    }

    public String getPassword() {
        return member.getPassword();
    }

    public Integer getAge() {
        return member.getAge();
    }
}