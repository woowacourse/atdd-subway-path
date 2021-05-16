package wooteco.subway.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dao.MemberDao;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    MemberDao memberDao;

    @InjectMocks
    MemberService memberService;


    @Test
    @DisplayName("토큰 생성 테스트")
    public void createToken() {
        //given
        String email = "abc";
        String password = "password";
        TokenRequest tokenRequest = new TokenRequest(email, password);
        when(jwtTokenProvider.createToken(any())).thenReturn(password);

        //when
        TokenResponse tokenResponse = memberService.createToken(tokenRequest);

        //then
        assertThat(tokenResponse.getAccessToken()).isEqualTo(password);

    }

}