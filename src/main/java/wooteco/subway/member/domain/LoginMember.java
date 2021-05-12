package wooteco.subway.member.domain;

import wooteco.subway.member.dto.MemberResponse;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {}

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember from(MemberResponse memberResponse) {
        return new LoginMember(memberResponse.getId(), memberResponse.getEmail(), memberResponse.getAge());
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
