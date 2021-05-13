package wooteco.subway.member.domain;

public class LoginMember {
    private final Long id;

    public LoginMember(String id) {
        this(Long.valueOf(id));
    }

    public LoginMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
