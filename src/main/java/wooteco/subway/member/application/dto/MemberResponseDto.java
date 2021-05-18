package wooteco.subway.member.application.dto;

import wooteco.subway.member.domain.Member;

public class MemberResponseDto {

    private final Long id;
    private final String email;
    private final Integer age;

    public MemberResponseDto(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getId(), member.getEmail(), member.getAge());
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
