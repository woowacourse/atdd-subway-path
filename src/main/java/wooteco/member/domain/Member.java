package wooteco.member.domain;

import org.springframework.http.HttpStatus;
import wooteco.exception.HttpException;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer age;

    public Member(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age);
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

    public void validatePassword(String passwordToCheck) {
        if (!password.equals(passwordToCheck)) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "로그인 정보가 올바르지 않습니다.");
        }
    }
}
