package wooteco.subway.member.domain;

public class LoginMember {
    private final Long id;
    private final String email;
    private final int age;

    public LoginMember(Member member) {
        this(member.getId(), member.getEmail(), member.getAge());
    }

    public LoginMember(Long id, String email, int age) {
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

    public int getAge() {
        return age;
    }
}
