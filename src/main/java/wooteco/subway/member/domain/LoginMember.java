package wooteco.subway.member.domain;

public class LoginMember {

    private final long id;
    private final String email;
    private final int age;

    public LoginMember(long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}
