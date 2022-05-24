package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@SpringBootTest
@Transactional
@Sql("classpath:setupPath.sql")
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @DisplayName("경로를 조회한다.")
    @Test
    void showRoute() {
        Long sourceStationId = 1L;
        Long targetStationId = 4L;
        int age = 20;

        PathResponse pathResponse = pathService.createPath(sourceStationId, targetStationId, age);

        assertAll(
                () -> assertThat(createStation(pathResponse))
                        .containsExactly(
                                new Station(1L, "이대역"),
                                new Station(2L, "학동역"),
                                new Station(3L, "이수역"),
                                new Station(4L, "건대역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    private List<Station> createStation(final PathResponse pathResponse) {
        return pathResponse.getStations().stream()
                .map(it -> new Station(it.getId(), it.getName()))
                .collect(Collectors.toList());

    }
}
