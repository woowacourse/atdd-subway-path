package wooteco.subway.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;
import wooteco.subway.member.dto.MemberRequest;
import wooteco.subway.member.dto.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("회원 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberDao memberDao;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("새로운 회원 생성")
    void createMember() {
        // given
        given(memberDao.insert(new Member("air.junseo@gmail.com", "1234", 26)))
                .willReturn(new Member(1L, "air.junseo@gmail.com", "1234", 26));

        // when
        MemberResponse response =
                memberService.createMember(new MemberRequest("air.junseo@gmail.com", "1234", 26));

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(new MemberResponse(1L, "air.junseo@gmail.com", 26));
    }

    @Test
    @DisplayName("회원 찾기")
    void findMember() {
        // given
        given(memberDao.findById(1L))
                .willReturn(new Member(1L, "air.junseo@gmail.com", "1234", 26));

        // when
        MemberResponse response = memberService.findMember(1L);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(new MemberResponse(1L, "air.junseo@gmail.com", 26));
    }

    @Test
    @DisplayName("회원 정보 업데이트")
    void updateMember() {
        // given
        Long id = 1L;
        MemberRequest request = new MemberRequest("air.junseo@gmail.com", "1234", 26);

        // when
        memberService.updateMember(id, request);

        // then
        verify(memberDao).update(new Member(id, "air.junseo@gmail.com", "1234", 26));
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMember() {
        // given
        Long id = 1L;

        // when
        memberService.deleteMember(id);

        // then
        verify(memberDao).deleteById(id);
    }
}