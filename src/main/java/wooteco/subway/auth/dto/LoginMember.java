package wooteco.subway.auth.dto;

import wooteco.subway.member.domain.Member;

public class LoginMember {

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {}

    public LoginMember(Member member) {
        this(member.getId(), member.getEmail(), member.getAge());
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
