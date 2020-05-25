package wooteco.subway.admin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.exception.IllegalPathRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Sql("/truncate.sql")
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @DisplayName("출발역과 도착역이 같은경우 Exception 발생")
    @Test
    void findPathException() {
        //given
        PathRequest pathRequest = new PathRequest("distance", "신촌", "신촌");

        //then
        assertThatThrownBy(() -> pathService.findPath(pathRequest))
                .isInstanceOf(IllegalPathRequestException.class)
                .hasMessage("출발역과 도착역이 같습니다.(%s)", "신촌");
    }

}