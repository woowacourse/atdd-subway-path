package wooteco.subway.member.domain;

public class LoginMember {

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember from(final Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
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
