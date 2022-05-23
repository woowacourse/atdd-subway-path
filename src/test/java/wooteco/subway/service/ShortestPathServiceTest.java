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
class ShortestPathServiceTest {

    @Autowired
    private PathService pathService;

    @Test
    @DisplayName("출발지, 도착지, 성인 나이가 주어질 때 경로와 요금을 계산한다.")
    void findPathWithAdult() {
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(5L, 6L, 20));

        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(5L, "강남역"),
                                tuple(6L, "청계산입구역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2250)
        );
    }

    @Test
    @DisplayName("출발지, 도착지, 청소년 나이가 주어질 때 경로와 요금을 계산한다.")
    void findPathWithTeenager() {
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(5L, 6L, 18));

        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(5L, "강남역"),
                                tuple(6L, "청계산입구역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1870)
        );
    }

    @Test
    @DisplayName("출발지, 도착지, 어린이 나이가 주어질 때 경로와 요금을 계산한다.")
    void findPathWithChild() {
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(5L, 6L, 12));

        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(5L, "강남역"),
                                tuple(6L, "청계산입구역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1300)
        );
    }

    @Test
    @DisplayName("출발지와 도착지가 등록되지 않은 지하철역일 경우에 예외 발생")
    void findPathWithNotFoundStation() {
        assertThatThrownBy(() -> pathService.findShortestPath(new PathRequest(0L, 1L, 20)))
                .isInstanceOf(NotFoundStationException.class)
                .hasMessageContaining("해당 지하철역이 등록이 안되어 있습니다.");
    }

    @Test
    @DisplayName("출발지와 도착지가 주어질 때 경로와 요금을 계산한다. - 환승")
    void findPathWithTransfer() {
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(1L, 7L, 20));

        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(1L, "신도림역"),
                                tuple(2L, "왕십리역"),
                                tuple(7L, "상일동역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(80),
                () -> assertThat(pathResponse.getFare()).isEqualTo(3450)
        );
    }

    @Test
    @DisplayName("출발지와 도착지가 반대로 주어질 때 경로와 요금을 계산한다.")
    void findPathWithReverseStations() {
        PathResponse pathResponse = pathService.findShortestPath(new PathRequest(7L, 1L, 20));

        assertAll(
                () -> assertThat(pathResponse.getStations())
                        .extracting("id", "name")
                        .containsExactly(
                                tuple(7L, "상일동역"),
                                tuple(2L, "왕십리역"),
                                tuple(1L, "신도림역")
                        ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(80),
                () -> assertThat(pathResponse.getFare()).isEqualTo(3450)
        );
    }
}
