package wooteco.member.domain;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer age;

    private Member(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static Member of(String email, String password, Integer age) {
        return new Member(null, email, password, age);
    }

    public static Member of(Long id, String email, String password, Integer age) {
        return new Member(id, email, password, age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }
}
