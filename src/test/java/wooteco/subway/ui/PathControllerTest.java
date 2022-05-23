package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.PathResponse;
import wooteco.subway.ui.dto.StationResponse;

class PathControllerTest extends ControllerTest {

    @Autowired
    private PathController pathController;

    @DisplayName("최단 경로의 지하철 역들과 거리, 운임 비용을 응답한다.")
    @Test
    void findShortestPath() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 5));

        // when
        ResponseEntity<PathResponse> response = pathController.findShortestPath(station1.getId(), station3.getId(), 20);

        // then
        HttpStatus statusCode = response.getStatusCode();
        PathResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId(), station3.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName(), station3.getName()),
                () -> assertThat(actual.getDistance()).isEqualTo(10),
                () -> assertThat(actual.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("추가 요금이 존재하는 최단 경로의 경유역들과 거리, 운임비용을 반환한다.")
    @Test
    void findShortestPathWithExtraFare() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 900));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 5));

        // when
        ResponseEntity<PathResponse> response = pathController.findShortestPath(station1.getId(), station3.getId(), 20);

        // then
        HttpStatus statusCode = response.getStatusCode();
        PathResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId(), station3.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName(), station3.getName()),
                () -> assertThat(actual.getDistance()).isEqualTo(10),
                () -> assertThat(actual.getFare()).isEqualTo(2150)
        );
    }

    @DisplayName("최단 경로의 경유역들과 거리, 어린이 운임비용을 반환한다.")
    @Test
    void findChildrenPolicyShortestPath() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 5));

        // when
        ResponseEntity<PathResponse> response = pathController.findShortestPath(station1.getId(), station3.getId(), 10);

        // then
        HttpStatus statusCode = response.getStatusCode();
        PathResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId(), station3.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName(), station3.getName()),
                () -> assertThat(actual.getDistance()).isEqualTo(10),
                () -> assertThat(actual.getFare()).isEqualTo(450)
        );
    }

    @DisplayName("추가 요금이 존재하는 최단 경로의 경유역들과 거리, 어린이 운임비용을 반환한다.")
    @Test
    void findChildrenPolicyShortestPathWithExtraFare() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 900));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 5));

        // when
        ResponseEntity<PathResponse> response = pathController.findShortestPath(station1.getId(), station3.getId(), 10);

        // then
        HttpStatus statusCode = response.getStatusCode();
        PathResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId(), station3.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName(), station3.getName()),
                () -> assertThat(actual.getDistance()).isEqualTo(10),
                () -> assertThat(actual.getFare()).isEqualTo(900)
        );
    }

    @DisplayName("최단 경로의 경유역들과 거리, 청소년 운임비용을 반환한다.")
    @Test
    void findTeenagerPolicyShortestPath() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 5));

        // when
        ResponseEntity<PathResponse> response = pathController.findShortestPath(station1.getId(), station3.getId(), 15);

        // then
        HttpStatus statusCode = response.getStatusCode();
        PathResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId(), station3.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName(), station3.getName()),
                () -> assertThat(actual.getDistance()).isEqualTo(10),
                () -> assertThat(actual.getFare()).isEqualTo(720)
        );
    }

    @DisplayName("추가 요금이 존재하는 최단 경로의 경유역들과 거리, 청소년 운임비용을 반환한다.")
    @Test
    void findTeenagerPolicyShortestPathWithExtraFare() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 900));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 5));

        // when
        ResponseEntity<PathResponse> response = pathController.findShortestPath(station1.getId(), station3.getId(), 15);

        // then
        HttpStatus statusCode = response.getStatusCode();
        PathResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId(), station3.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName(), station3.getName()),
                () -> assertThat(actual.getDistance()).isEqualTo(10),
                () -> assertThat(actual.getFare()).isEqualTo(1440)
        );
    }

    @DisplayName("최단 경로의 경유역들과 거리, 우대 운임비용을 반환한다.")
    @Test
    void findPreferentialPolicyShortestPath() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 5));

        // when
        ResponseEntity<PathResponse> response = pathController.findShortestPath(station1.getId(), station3.getId(), 5);

        // then
        HttpStatus statusCode = response.getStatusCode();
        PathResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId(), station3.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName(), station3.getName()),
                () -> assertThat(actual.getDistance()).isEqualTo(10),
                () -> assertThat(actual.getFare()).isEqualTo(0)
        );
    }

    @DisplayName("추가 요금이 존재하는 최단 경로의 경유역들과 거리, 우대 운임비용을 반환한다.")
    @Test
    void findPreferentialPolicyShortestPathWithExtraFare() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 900));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 5));

        // when
        ResponseEntity<PathResponse> response = pathController.findShortestPath(station1.getId(), station3.getId(), 65);

        // then
        HttpStatus statusCode = response.getStatusCode();
        PathResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId(), station3.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName(), station3.getName()),
                () -> assertThat(actual.getDistance()).isEqualTo(10),
                () -> assertThat(actual.getFare()).isEqualTo(0)
        );
    }

    @DisplayName("구간에 등록되지않은 지하철역으로 최단 경로 조회시 예외가 발생한다.")
    @Test
    void findShortestPath_exceptionNotSavedInSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 900));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 5));

        // when
        assertThatThrownBy(() -> pathController.findShortestPath(station1.getId(), station3.getId(), 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간에 등록 되지 않은 역입니다.");
    }

    @DisplayName("연결되지 않은 구간의 최단 경로 조회시 예외가 발생한다.")
    @Test
    void findShortestPath_exceptionInvalidPath() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Station station4 = stationDao.save(new Station("성수역역"));
        Line line1 = lineDao.save(new Line("2호선", "green", 900));
        sectionDao.save(new Section(line1.getId(), station1.getId(), station2.getId(), 5));
        Line line2 = lineDao.save(new Line("3호선", "orange", 900));
        sectionDao.save(new Section(line2.getId(), station3.getId(), station4.getId(), 5));

        // when
        assertThatThrownBy(() -> pathController.findShortestPath(station1.getId(), station3.getId(), 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("연결되지 않은 구간입니다.");
    }
}
