package wooteco.subway.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.application.AuthorizedException;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.Member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberDao memberDao;

    private final String testEmail = "test";
    private final String testPassword = "test";

    @DisplayName("db에 존재하는 사용자로 token을 생성할 경우 정상 생성된다.")
    @Test
    public void checkIsExisting() {
        final TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);
        memberDao.insert(new Member(null, testEmail, testPassword, 1));

        authService.createToken(tokenRequest);
    }

    @DisplayName("db에 존재하지 않는 사용자로 token을 생성할 경우 예외를 발생한다.")
    @Test
    public void checkIsExistingWhenDataNotExisting() {
        final TokenRequest tokenRequest = new TokenRequest(testEmail, testPassword);

        assertThatThrownBy(() -> {
            authService.createToken(tokenRequest);
        }).isInstanceOf(AuthorizedException.class);
    }
}
