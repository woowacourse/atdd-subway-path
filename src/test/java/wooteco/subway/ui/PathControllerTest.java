package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.DomainException;

@SpringBootTest
@Transactional
@Sql("classpath:testInit.sql")
class PathControllerTest {

    @Autowired
    PathController pathController;

    @Test
    @DisplayName("경로를 구한다.")
    void getPath() {
        PathResponse path = pathController.getPath(1L, 3L, 20);

        assertAll(() -> {
            assertThat(path.getFare()).isEqualTo(1450L);
            assertThat(path.getDistance()).isEqualTo(10);
            assertThat(path.getStations()).hasSize(3);
        });
    }

    @Test
    @DisplayName("연결되지 않은 역을 경로를 구하려고 하면 예외")
    void getPath_invalid() {
        assertThatThrownBy(() -> pathController.getPath(1L, 4L, 20))
                .isInstanceOf(DomainException.class);
    }
}
