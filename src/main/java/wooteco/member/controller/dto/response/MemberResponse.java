package wooteco.member.controller.dto.response;

import wooteco.member.controller.dto.request.ApprovedMemberRequest;
import wooteco.member.domain.Member;

public class MemberResponse {
    private Long id;
    private String email;
    private Integer age;

    private MemberResponse() {
    }

    private MemberResponse(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse from(ApprovedMemberRequest approvedMemberRequest) {
        return new MemberResponse(approvedMemberRequest.getId(), approvedMemberRequest.getEmail(),
                approvedMemberRequest.getAge());
    }

    public static MemberResponse from(Member member) {
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
