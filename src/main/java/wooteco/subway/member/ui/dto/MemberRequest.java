package wooteco.subway.member.ui.dto;

import wooteco.subway.member.ui.dto.valid.NumberValidation;
import wooteco.subway.member.ui.dto.valid.StringValidation;

import java.beans.ConstructorProperties;

public class MemberRequest {

    @StringValidation
    private final String email;
    @StringValidation
    private final String password;
    @NumberValidation
    private final Integer age;

    @ConstructorProperties({"email", "password", "age"})
    public MemberRequest(String email, String password, Integer age) {
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
