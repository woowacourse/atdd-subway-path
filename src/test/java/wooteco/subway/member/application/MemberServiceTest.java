package wooteco.subway.member.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.MemberNotFoundException;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@ActiveProfiles("test")
@Sql("classpath:/test-schema.sql")
@SpringBootTest
@Transactional
class MemberServiceTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";
    private static final int AGE = 12;

    private final MemberService memberService;

    @Autowired
    public MemberServiceTest(MemberService memberService) {
        this.memberService = memberService;
    }

    @Test
    void createMember() {
        //given
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);

        //when
        MemberResponse memberResponse = memberService.createMember(memberRequest);

        //then
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(memberRequest.getEmail());
        assertThat(memberResponse.getAge()).isEqualTo(memberRequest.getAge());
    }

    @Test
    void duplicateCreateMember() {
        //given
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        memberService.createMember(memberRequest);

        //then
        assertThatThrownBy(() -> memberService.createMember(memberRequest))
            .isInstanceOf(DuplicateEmailException.class).hasMessage("중복된 이메일 입니다. " + EMAIL);
    }

    @Test
    void findMember() {
        //given
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        MemberResponse createdMember = memberService.createMember(memberRequest);

        //when
        MemberResponse memberResponse = memberService.findMember(createdMember.getId());

        //then
        assertThat(memberResponse.getEmail()).isEqualTo(EMAIL);
        assertThat(memberResponse.getAge()).isEqualTo(AGE);
    }

    @Test
    void updateMember() {
        //given
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        MemberResponse createdMember = memberService.createMember(memberRequest);
        MemberRequest newMember = new MemberRequest(EMAIL, PASSWORD, AGE + 1);

        //when
        memberService.updateMember(createdMember.getId(), newMember);

        //then
        assertThat(memberService.findMember(createdMember.getId()).getAge()).isEqualTo(AGE + 1);
    }

    @Test
    void deleteMember() {
        //given
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        MemberResponse memberResponse = memberService.createMember(memberRequest);

        //when
        memberService.deleteMember(memberResponse.getId());

        //then
        assertThatThrownBy(() -> memberService.findMember(memberResponse.getId()))
            .isInstanceOf(MemberNotFoundException.class);
    }
}