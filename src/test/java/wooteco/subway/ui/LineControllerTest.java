package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.LineResponse;
import wooteco.subway.ui.dto.StationResponse;

class LineControllerTest extends ControllerTest {

    @Autowired
    private LineController lineController;

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));

        LineRequest lineRequest = new LineRequest("2호선", "green", station1.getId(), station2.getId(), 10, 0);

        // when
        ResponseEntity<LineResponse> response = lineController.createLine(lineRequest);

        // then
        HttpStatus statusCode = response.getStatusCode();
        LineResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getName()).isEqualTo(lineRequest.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(lineRequest.getColor()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getId(), station2.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(station1.getName(), station2.getName()),
                () -> assertThat(actual.getExtraFare()).isEqualTo(lineRequest.getExtraFare())
        );
    }

    @DisplayName("기존에 존재하는 노선의 이름으로 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));

        LineRequest lineRequest = new LineRequest("2호선", "green", station1.getId(), station2.getId(), 10, 0);
        lineController.createLine(lineRequest);

        // when
        assertThatThrownBy(() -> lineController.createLine(lineRequest))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        Line savedLine1 = lineDao.save(new Line("2호선", "green", 0));
        Line savedLine2 = lineDao.save(new Line("3호선", "orange", 100));

        // when
        ResponseEntity<List<LineResponse>> response = lineController.showLines();

        // then
        HttpStatus statusCode = response.getStatusCode();
        List<LineResponse> actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.stream()
                        .map(LineResponse::getId))
                        .containsExactly(savedLine1.getId(), savedLine2.getId()),
                () -> assertThat(actual.stream()
                        .map(LineResponse::getName))
                        .containsExactly(savedLine1.getName(), savedLine2.getName()),
                () -> assertThat(actual.stream()
                        .map(LineResponse::getColor))
                        .containsExactly(savedLine1.getColor(), savedLine2.getColor()),
                () -> assertThat(actual.stream()
                        .map(LineResponse::getStations))
                        .size()
                        .isEqualTo(2),
                () -> assertThat(actual.stream()
                        .map(LineResponse::getExtraFare))
                        .containsExactly(savedLine1.getExtraFare(), savedLine2.getExtraFare())
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));

        // when
        ResponseEntity<LineResponse> response = lineController.showLine(line.getId());

        // then
        HttpStatus statusCode = response.getStatusCode();
        LineResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getId()).isEqualTo(line.getId()),
                () -> assertThat(actual.getName()).isEqualTo(line.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(line.getColor()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getId))
                        .containsExactly(station1.getId(), station2.getId()),
                () -> assertThat(actual.getStations()
                        .stream()
                        .map(StationResponse::getName))
                        .containsExactly(station1.getName(), station2.getName()),
                () -> assertThat(actual.getExtraFare()).isEqualTo(line.getExtraFare())
        );
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회하면 예외가 발생한다.")
    @Test
    void showNonexistentLine() {
        // when
        assertThatThrownBy(() -> lineController.showLine(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));

        // when
        LineRequest lineRequest = new LineRequest("3호선", "orange", 1L, 2L, 1, 100);
        ResponseEntity<Void> response = lineController.updateLine(line.getId(), lineRequest);

        // then
        HttpStatus statusCode = response.getStatusCode();
        LineResponse actual = lineController.showLine(line.getId()).getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.getId()).isEqualTo(line.getId()),
                () -> assertThat(actual.getName()).isEqualTo(lineRequest.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(lineRequest.getColor()),
                () -> assertThat(actual.getExtraFare()).isEqualTo(lineRequest.getExtraFare())
        );
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));

        // when
        ResponseEntity<Void> response = lineController.deleteLine(line.getId());

        // then
        HttpStatus statusCode = response.getStatusCode();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT),
                () -> assertThatThrownBy(() -> lineController.showLine(line.getId()))
                        .isInstanceOf(EmptyResultDataAccessException.class)
        );
    }
}
