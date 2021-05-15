package wooteco.member.controller.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MemberRequestDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Integer age;

    public MemberRequestDto() {
    }

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
