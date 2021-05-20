package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

public class TranslatedMemberRequest {

    private Long id;
    private String email;
    private Integer age;

    public TranslatedMemberRequest () {
    }

    public TranslatedMemberRequest(MemberResponse memberResponse) {
        this.id = memberResponse.getId();
        this.email = memberResponse.getEmail();
        this.age = memberResponse.getAge();
    }

    public TranslatedMemberRequest (Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public MemberResponse toResponse() {
        return new MemberResponse(id, email, age);
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
