package wooteco.subway.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.dao.MemberDao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private MemberDao mockMemberDao;

    private final String testEmail = "test";
    private final String testPassword = "test";

    @DisplayName("db에 존재하는 사용자로 token을 생성할 경우 예외를 발생한다.")
    @Test
    public void checkIsExisting(){
        final TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);

        Mockito.when(mockMemberDao.isExist(testEmail, testPassword))
                .thenReturn(true);

        authService.createToken(tokenRequest);
    }

    @DisplayName("db에 존재하지 않는 사용자로 token을 생성할 경우 예외를 발생한다.")
    @Test
    public void checkIsExistingWhenDataNotExisting(){
        final TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);

        assertThatThrownBy(()->{
            authService.createToken(tokenRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
