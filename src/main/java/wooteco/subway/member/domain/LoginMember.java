package wooteco.subway.member.domain;

public class LoginMember {
    private Long id;
    private String email;

    public LoginMember(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public LoginMember(String id, String email) {
        this(Long.valueOf(id), email);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
