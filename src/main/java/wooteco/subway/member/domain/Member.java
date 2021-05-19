package wooteco.subway.member.domain;

import wooteco.subway.member.exception.NotSamePasswordException;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {
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

    public boolean samePassword(String password) {
        return this.password.equals(password);
    }

    public void validatePassword(String password) {
        if (!samePassword(password)) {
            throw new NotSamePasswordException();
        }
    }
}
