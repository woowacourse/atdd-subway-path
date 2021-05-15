package wooteco.subway.member.domain;

import wooteco.subway.member.dto.MemberResponse;

public class LoginMember {

    private Long id;

    public LoginMember() {
    }

    public LoginMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
