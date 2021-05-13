package wooteco.subway.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    JwtTokenProvider jwtTokenProvider;
    @Mock
    MemberDao memberDao;

    @InjectMocks
    AuthService authService;


    @Test
    @DisplayName("토큰 생성 테스트")
    public void createToken() {
        //given
        String email = "abc";
        String password = "password";
        TokenRequest tokenRequest = new TokenRequest(email, password);
        when(memberDao.findByEmail(any())).thenReturn(Optional.of(new Member(email, password, 27)));
        when(jwtTokenProvider.createToken(any())).thenReturn(password);

        //when
        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        //then
        assertThat(tokenResponse.getAccessToken()).isEqualTo(password);

    }

}