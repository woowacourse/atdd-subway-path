package wooteco.subway.member.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.MemberNotFoundException;
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

    @DisplayName("정상적인 회원 등록 요청한다.")
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

    @DisplayName("이미 등록된 email 아이디로 회원 등록 요청")
    @Test
    void duplicateCreateMember() {
        //given
        회원_생성_요청();

        //then
        assertThatThrownBy(this::회원_생성_요청)
            .isInstanceOf(DuplicateEmailException.class).hasMessage("중복된 이메일 입니다.");
    }

    @DisplayName("ID로 회원을 검색한다.")
    @Test
    void findMember() {
        //given
        MemberResponse 생성된_회원 = 회원_생성_요청();

        //when
        MemberResponse 검색해서_찾은_회원 = memberService.findMember(생성된_회원.getId());

        //then
        assertThat(검색해서_찾은_회원).isNotNull();
        assertThat(검색해서_찾은_회원).isInstanceOf(MemberResponse.class);
    }

    @DisplayName("존재하지 않는 ID로 회원을 검색한다.")
    @Test
    void findNotExistMember() {
        //given
        회원_생성_요청();
        Long wrongId = 99999L;

        //then
        assertThatThrownBy(() -> {
            memberService.findMember(wrongId);
        }).isInstanceOf(MemberNotFoundException.class).hasMessage("회원이 존재하지 않습니다.");
    }

    @DisplayName("회원 정보 수정 요청한다.")
    @Test
    void updateMember() {
        //given
        MemberResponse 생성된_회원 = 회원_생성_요청();
        int 내가_돌아가고싶은_나이 = 20;
        MemberRequest memberRequest = new MemberRequest("updated@email.com", "updated", 내가_돌아가고싶은_나이);
        Long id = 생성된_회원.getId();

        //when
        memberService.updateMember(생성된_회원.getId(), memberRequest);
        MemberResponse 수정된_회원 = memberService.findMember(id);

        //then
        assertThat(수정된_회원.getId()).isEqualTo(id);
        assertThat(수정된_회원.getAge()).isEqualTo(memberRequest.getAge());
        assertThat(수정된_회원.getEmail()).isEqualTo(memberRequest.getEmail());
    }

    @DisplayName("이미 존재하는 Email로 수정 한다.")
    @Test
    void duplicateEmailUpdate() {
        //given
        MemberResponse 생성된_회원 = 회원_생성_요청();
        MemberRequest memberRequest = new MemberRequest("me@email.com", "updated", 20);
        회원_생성_요청(memberRequest);

        //then
        assertThatThrownBy(() -> {
            memberService.updateMember(생성된_회원.getId(), memberRequest);
        }).isInstanceOf(DuplicateEmailException.class).hasMessage("중복된 이메일 입니다.");
    }

    @DisplayName("존재하는 회원 삭제한다.")
    @Test
    void deleteMember() {
        //given
        MemberResponse 생성된_회원 = 회원_생성_요청();
        Long id = 생성된_회원.getId();

        //when
        memberService.deleteMember(id);

        //then
        assertThatThrownBy(() -> {
            memberService.findMember(id);
        }).isInstanceOf(MemberNotFoundException.class).hasMessage("회원이 존재하지 않습니다.");
    }

    private MemberResponse 회원_생성_요청() {
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        return memberService.createMember(memberRequest);
    }

    private MemberResponse 회원_생성_요청(MemberRequest memberRequest) {
        return memberService.createMember(memberRequest);
    }
}