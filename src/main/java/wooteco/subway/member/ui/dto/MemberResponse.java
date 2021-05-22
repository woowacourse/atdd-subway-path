package wooteco.subway.member.ui.dto;

import wooteco.subway.member.application.dto.MemberResponseDto;
import wooteco.subway.member.ui.dto.valid.NumberValidation;
import wooteco.subway.member.ui.dto.valid.StringValidation;

import java.beans.ConstructorProperties;

public class MemberResponse {

    @NumberValidation
    private final Long id;
    @StringValidation
    private final String email;
    @NumberValidation
    private final Integer age;

    @ConstructorProperties({"id", "email", "age"})
    public MemberResponse(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public MemberResponse(MemberResponseDto memberResponseDto) {
        this(
                memberResponseDto.getId(),
                memberResponseDto.getEmail(),
                memberResponseDto.getAge()
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
