package wooteco.member.controller.dto.response;

import wooteco.member.domain.Member;

public class MemberResponseDto {
    private Long id;
    private String email;
    private Integer age;

    public MemberResponseDto() {
    }

    public MemberResponseDto(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public MemberResponseDto(Member member) {
        id = member.getId();
        email = member.getEmail();
        age = member.getAge();
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
