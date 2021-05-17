package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.auth.application.exception.AuthorizationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("회원 도메인 테스트")
class MemberTest {

    @Test
    @DisplayName("유효한 비밀 번호가 아닌 경우")
    void validatePassword() {
        // given
        Member member = new Member(1L, "air.junseo@gmail.com", "1234", 26);

        // when & then
        assertThatThrownBy(() -> member.validatePassword("12345"))
                .isInstanceOf(AuthorizationException.class);
    }
}