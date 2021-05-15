package wooteco.member.controller.dto.request;

import wooteco.member.domain.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApprovedMemberRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Integer age;

    public ApprovedMemberRequest() {

    }

    public ApprovedMemberRequest(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static ApprovedMemberRequest from(Member member) {
        return new ApprovedMemberRequest(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public Long getId() {
        return id;
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
