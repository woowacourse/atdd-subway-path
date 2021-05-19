package wooteco.subway.member.domain;

import wooteco.subway.auth.exception.UnauthorizedException;
import wooteco.subway.auth.infrastructure.PasswordHasher;

import java.util.Objects;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Integer age;
    private String salt;

    public Member() {
    }

    public Member(Long id, String email, Integer age) {
        this(id, email, null, age, null);
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age, null);
    }

    public Member(Long id, String email, String password, Integer age) {
        this(id, email, password, age, null);
    }

    public Member(Long id, String email, String password, Integer age, String salt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
        this.salt = salt;
    }

    public Member encryptPassword() {
        String salt = PasswordHasher.createSalt();
        if (this.password == null || this.password.isEmpty()) {
            throw new UnauthorizedException("패스워드가 없습니다.");
        }
        String hashingPassword = PasswordHasher.hashing(this.password, salt);
        return new Member(this.getId(), this.getEmail(), hashingPassword, this.getAge(), salt);
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

    public String getSalt() {
        return salt;
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
