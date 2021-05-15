package wooteco.subway.member.domain;

import wooteco.subway.member.application.AuthorizationException;

public class Member {

    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Member(String email, String password, Integer age) {
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

    public void authorize(Member requestMember) {
        final boolean isEmailEqual = email.equals(requestMember.email);
        final boolean isPasswordEqual = password.equals(requestMember.password);
        if (!(isEmailEqual && isPasswordEqual)) {
            throw new AuthorizationException("이메일 또는 비밀번호가 틀립니다.");
        }
    }
}
