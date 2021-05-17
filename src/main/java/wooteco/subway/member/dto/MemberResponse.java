package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

import java.beans.ConstructorProperties;

public class MemberResponse {
    private Long id;
    private String email;
    private Integer age;

    @ConstructorProperties({"id", "email", "age"})
    public MemberResponse(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public MemberResponse(Member member) {
        this(member.getId(), member.getEmail(), member.getAge());
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
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
