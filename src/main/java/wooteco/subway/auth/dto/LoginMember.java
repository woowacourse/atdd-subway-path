package wooteco.subway.auth.dto;

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
