package wooteco.subway.member.domain;

import java.util.regex.Pattern;

public class Member {

    private static final String EMAIL_PATTERN = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    private static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}";

    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member(String email, String password, Integer age) {
        validate(email, password);
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(Long id, String email, String password, Integer age) {
        this(email, password, age);
        this.id = id;
    }

    private void validate(String email, String password) {
        validateEmailFormat(email);
        validatePasswordFormat(password);
    }

    private void validateEmailFormat(String email) {
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            throw new IllegalArgumentException("올바르지 않은 이메일 형식 입니다.");
        }
    }

    private void validatePasswordFormat(String password) {
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            throw new IllegalArgumentException(
                "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.");
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
}
