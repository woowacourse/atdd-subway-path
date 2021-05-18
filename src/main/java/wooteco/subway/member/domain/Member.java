package wooteco.subway.member.domain;

import java.util.function.Function;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(Long id, Member member) {
        this(id, member.email, member.password, member.age);
    }

    public Member(Long id, String email, Integer age) {
        this(id, email, null, age);
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age);
    }

    public Member(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
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

    public Member newInstanceWithHashPassword(Function<String, String> hashFunction) {
        String hashedPassword = hashFunction.apply(password);
        return new Member(id, email, hashedPassword, age);
    }
}
