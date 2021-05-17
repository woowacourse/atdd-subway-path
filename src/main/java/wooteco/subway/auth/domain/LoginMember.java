package wooteco.subway.auth.domain;

import wooteco.subway.member.dto.MemberResponse;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public LoginMember(final MemberResponse memberResponse) {
        this(memberResponse.getId(), memberResponse.getEmail(), memberResponse.getAge());
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
