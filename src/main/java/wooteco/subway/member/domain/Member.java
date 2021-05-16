package wooteco.subway.member.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return getId().equals(member.getId()) && Objects.equals(getEmail(), member.getEmail()) && Objects.equals(getPassword(), member.getPassword()) && Objects.equals(getAge(), member.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword(), getAge());
    }
}
