package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.exception.NotFoundStationException;

@SpringBootTest
@Transactional
@Sql("/pathInitSchema.sql")
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Test
    @DisplayName("출발지와 도착지가 주어질 때 경로와 요금을 계산한다.")
    void findPath() {
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(5L, 6L, 10));

        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(5L, "강남역"),
                                tuple(6L, "청계산입구역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    @Test
    @DisplayName("출발지와 도착지가 등록되지 않은 지하철역일 경우에 예외 발생")
    void findPathWithNotFoundStation() {
        assertThatThrownBy(() -> pathService.findShortestPath(new PathRequest(0L, 1L, 10)))
                .isInstanceOf(NotFoundStationException.class)
                .hasMessageContaining("존재하지 않는 지하철 역입니다.");
    }

    @Test
    @DisplayName("출발지와 도착지가 주어질 때 경로와 요금을 계산한다. - 환승")
    void findPathWithTransfer() {
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(1L, 7L, 10));

        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(1L, "신도림역"),
                                tuple(2L, "왕십리역"),
                                tuple(7L, "상일동역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(80),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2450)
        );
    }

    @Test
    @DisplayName("출발지와 도착지가 반대로 주어질 때 경로와 요금을 계산한다.")
    void findPathWithReverseStations() {
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(7L, 1L, 10));

        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(7L, "상일동역"),
                                tuple(2L, "왕십리역"),
                                tuple(1L, "신도림역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(80),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2450)
        );
    }
}
