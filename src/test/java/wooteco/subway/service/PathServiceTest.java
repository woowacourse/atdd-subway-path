package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.Path;

@SpringBootTest
@Transactional
@Sql("classpath:testInit.sql")
public class PathServiceTest {

    @Autowired
    private PathService pathService;


    @Test
    @DisplayName("경로 조회한다")
    void getPath() {
        // given
        Path path = pathService.getPath(3L, 1L);

        // then
        assertThat(path.getStations()).hasSize(3);
        assertThat(path.getDistance()).isEqualTo(10);
    }

    void getFare() {
        Fare fare = pathService.getFare(3L, 1L, 20);

        assertThat(fare.getValue()).isEqualTo(1450L);
    }
}
