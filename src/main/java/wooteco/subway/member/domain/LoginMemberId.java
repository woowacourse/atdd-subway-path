package wooteco.subway.member.domain;

public class LoginMemberId {
    private Long id;

    public LoginMemberId() {
    }

    public LoginMemberId(String id) {
        this(Long.valueOf(id));
    }

    private LoginMemberId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
