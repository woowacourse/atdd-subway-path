package wooteco.subway.presentation.dto.response;

import wooteco.subway.domain.member.Member;
import wooteco.subway.presentation.dto.request.LoginMember;

public class MemberResponse {

    private Long id;
    private String email;
    private Integer age;

    public MemberResponse() {
    }

    public MemberResponse(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(LoginMember loginMember) {
        return MemberResponse.of(loginMember.toMember());
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getAge());
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
