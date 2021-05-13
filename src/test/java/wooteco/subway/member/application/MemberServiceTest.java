package wooteco.subway.member.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberServiceTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";
    private static final int AGE = 12;

    private MemberService memberService;

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
            .isInstanceOf(DuplicateEmailException.class).hasMessage("중복된 이메일 입니다.");
    }

    @Test
    void findMember() {
    }

    @Test
    void updateMember() {
    }

    @Test
    void deleteMember() {
    }
}