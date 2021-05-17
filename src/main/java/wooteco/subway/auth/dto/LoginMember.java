package wooteco.subway.auth.dto;

import wooteco.subway.member.domain.Member;

public class LoginMember {

    private Long id;
    private String email;
    private String password;
    private Integer age;

    public LoginMember(Member member) {
        this(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getAge());
    }

    public LoginMember(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member toMember() {
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
