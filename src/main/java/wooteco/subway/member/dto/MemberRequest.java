package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

public class MemberRequest {

    private Long id;
    private String email;
    private String password;
    private Integer age;

    public MemberRequest() {
    }

    public MemberRequest(String email) {
        this(null, email);
    }

    public MemberRequest(Long id, String email) {
        this(id, email, null, null);
    }

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public MemberRequest(Long id, String email, String password,Integer age) {
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

    public Member toMember() {
        return new Member(email, password, age);
    }
}
