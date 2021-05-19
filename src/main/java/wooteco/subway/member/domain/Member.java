package wooteco.subway.member.domain;

import wooteco.subway.auth.exception.UnauthorizedException;
import wooteco.subway.auth.infrastructure.PasswordHasher;

import java.util.Objects;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer age;
    private final String salt;

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

    public Member insertId(Long id, Member member) {
        return new Member(id, member.email, member.password, member.age, member.salt);
    }

    public void checkPassword(String password) {
        String hashing = PasswordHasher.hashing(password, this.salt);
        if (!Objects.equals(hashing, this.getPassword())) {
            throw new UnauthorizedException("비밀번호가 같지 않습니다.");
        }
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
        return Objects.equals(getId(), member.getId()) && Objects.equals(getEmail(), member.getEmail()) && Objects.equals(getPassword(), member.getPassword()) && Objects.equals(getAge(), member.getAge()) && Objects.equals(salt, member.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword(), getAge(), salt);
    }
}
