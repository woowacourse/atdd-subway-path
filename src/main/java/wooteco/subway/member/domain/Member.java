package wooteco.subway.member.domain;

import wooteco.subway.member.application.AuthorizationException;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer age;

    public Member(String email, String password) {
        this(null, email, password, null);
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

    public void authorize(final Member requestMember) {
        final boolean isEmailEqual = this.email.equals(requestMember.email);
        final boolean isPasswordEqual = this.password.equals(requestMember.password);
        if (!(isEmailEqual && isPasswordEqual)) {
            throw new AuthorizationException("이메일 또는 비밀번호가 틀립니다.");
        }
    }
}
