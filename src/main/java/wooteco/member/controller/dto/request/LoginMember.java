package wooteco.member.controller.dto.request;

public class LoginMember {
    private final Long id;

    public LoginMember(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
