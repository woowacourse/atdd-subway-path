package wooteco.subway.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import wooteco.subway.exception.badrequest.DuplicateEmailException;
import wooteco.subway.exception.notfound.MemberNotFoundException;
import wooteco.subway.exception.badrequest.NoRowHasBeenModifiedException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    private static final String EMAIL = "service@test.com";
    private static final String PASSWORD = "test";
    private static final int AGE = 12;
    private final Member 저장된_회원 = new Member(1L, EMAIL, PASSWORD, AGE);
    private final LoginMember 로그인된_회원 = new LoginMember(1L, "update@email.com", 10);
    private final MemberRequest 요청한_회원 = new MemberRequest("updated@email.com", "updated", 20);

    @Mock
    private MemberDao memberDao = mock(MemberDao.class);

    @InjectMocks
    private MemberService memberService;

    @DisplayName("정상적인 회원 등록 요청한다.")
    @Test
    void createMember() {
        //given
        MemberRequest 회원가입_요청 = new MemberRequest(EMAIL, PASSWORD, AGE);
        given(memberDao.insert(any(Member.class)))
                .willReturn(저장된_회원);

        //when
        MemberResponse memberResponse = memberService.createMember(회원가입_요청);

        //then
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getEmail()).isEqualTo(회원가입_요청.getEmail());
        assertThat(memberResponse.getAge()).isEqualTo(회원가입_요청.getAge());
        verify(memberDao).insert(any(Member.class));
    }

    @DisplayName("이미 등록된 email 아이디로 회원 등록 요청")
    @Test
    void duplicateCreateMember() {
        //given
        MemberRequest 회원가입_요청 = new MemberRequest(EMAIL, PASSWORD, AGE);
        given(memberDao.insert(any(Member.class)))
                .willThrow(DuplicateKeyException.class);

        //then
        assertThatThrownBy(() -> memberService.createMember(회원가입_요청))
                .isInstanceOf(DuplicateEmailException.class).hasMessage("중복된 이메일 입니다.");
        verify(memberDao).insert(any(Member.class));
    }

    @DisplayName("ID로 회원을 검색한다.")
    @Test
    void findMember() {
        //given
        given(memberDao.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(저장된_회원));

        //when
        MemberResponse 검색해서_찾은_회원 = memberService.findMember(1L);

        //then
        assertThat(검색해서_찾은_회원.getId()).isEqualTo(저장된_회원.getId());
        assertThat(검색해서_찾은_회원.getEmail()).isEqualTo(저장된_회원.getEmail());
        assertThat(검색해서_찾은_회원.getAge()).isEqualTo(저장된_회원.getAge());
        verify(memberDao).findById(any(Long.class));
    }

    @DisplayName("존재하지 않는 ID로 회원을 검색한다.")
    @Test
    void findNotExistMember() {
        //given
        given(memberDao.findById(1L))
                .willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> memberService.findMember(1L))
                .isInstanceOf(MemberNotFoundException.class).hasMessage("회원이 존재하지 않습니다.");
    }

    @DisplayName("회원 정보 수정 요청한다.")
    @Test
    void updateMember() {
        //given
        doNothing().when(memberDao).update(any(Member.class));

        //when
        memberService.updateMember(로그인된_회원, 요청한_회원);

        //then
        verify(memberDao).update(any(Member.class));
    }

    @DisplayName("존재하지 않는 회원의 정보를 수정한다.")
    @Test
    void notExistMemberUpdate() {
        //given
        doThrow(NoRowHasBeenModifiedException.class)
                .when(memberDao).update(any(Member.class));

        //when then
        assertThatThrownBy(() -> memberService.updateMember(로그인된_회원, 요청한_회원))
                .isInstanceOf(MemberNotFoundException.class).hasMessage("회원이 존재하지 않습니다.");
    }

    @DisplayName("이미 존재하는 Email 로 회원 Email을 수정한다.")
    @Test
    void duplicateEmailUpdate() {
        //given
        doThrow(DuplicateKeyException.class).when(memberDao).update(any(Member.class));

        //when
        assertThatThrownBy(() -> {
            memberService.updateMember(로그인된_회원, 요청한_회원);
        }).isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("존재하는 회원 삭제한다.")
    @Test
    void deleteMember() {
        //given
        doNothing().when(memberDao).deleteById(1L);

        //when
        memberService.deleteMember(1L);

        //then
        verify(memberDao).deleteById(1L);
    }

    @DisplayName("존재하지 않는 회원 삭제한다.")
    @Test
    void deleteNotExistMember() {
        //given
        doThrow(NoRowHasBeenModifiedException.class).when(memberDao).deleteById(1L);

        //when then
        assertThatThrownBy(() -> {
            memberService.deleteMember(1L);
        }).isInstanceOf(MemberNotFoundException.class).hasMessage("회원이 존재하지 않습니다.");
    }
}