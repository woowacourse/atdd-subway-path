package wooteco.subway.auth.domain;

import wooteco.subway.member.dto.MemberResponse;

public class LoginMember {
    private Long id;

    public LoginMember(Long id) {
        this.id = id;
    }

    public LoginMember(final MemberResponse memberResponse) {
        this(memberResponse.getId());
    }

    public Long getId() {
        return id;
    }
}
