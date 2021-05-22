package wooteco.subway.member.application.dto;

public class MemberRequestDto {

    private final String email;
    private final String password;
    private final Integer age;

    public MemberRequestDto(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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
