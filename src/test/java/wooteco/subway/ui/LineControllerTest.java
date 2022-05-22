package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.line.LineResponse;
import wooteco.subway.dto.station.StationResponse;
import wooteco.subway.exception.line.DuplicateLineException;
import wooteco.subway.exception.line.NoSuchLineException;

class LineControllerTest extends ControllerTest {

    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "green";

    @Autowired
    private LineController lineController;

    private Station gangnam;
    private Station yeoksam;
    private Station seolleung;
    private Station samsung;

    private Line greenLine;

    @BeforeEach
    void setUpData() {
        gangnam = stationDao.insert(new Station("강남역")).orElseThrow();
        yeoksam = stationDao.insert(new Station("역삼역")).orElseThrow();
        seolleung = stationDao.insert(new Station("선릉역")).orElseThrow();
        samsung = stationDao.insert(new Station("삼성역")).orElseThrow();

        greenLine = new Line(LINE_NAME, LINE_COLOR, 300);
    }

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void CreateLine() {
        // given
        final LineRequest request = new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                gangnam.getId(),
                yeoksam.getId(),
                10,
                0
        );

        final List<StationResponse> expectedStations = Stream.of(this.gangnam, yeoksam)
                .map(StationResponse::from)
                .collect(Collectors.toList());

        // when
        final ResponseEntity<LineResponse> response = lineController.createLine(request);

        final LineResponse actual = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actual.getName()).isEqualTo(LINE_NAME);
        assertThat(actual.getColor()).isEqualTo(LINE_COLOR);
        assertThat(actual.getStations()).isEqualTo(expectedStations);
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 노선을 생성하면 예외를 던진다.")
    void CreateLine_DuplicateName_ExceptionThrown() {
        // given
        lineDao.insert(new Line(LINE_NAME, LINE_COLOR));

        final LineRequest request = new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                gangnam.getId(),
                yeoksam.getId(),
                10,
                0
        );

        // when, then
        assertThatThrownBy(() -> lineController.createLine(request))
                .isInstanceOf(DuplicateLineException.class);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void ShowLines() {
        // given
        final Line purpleLine = new Line("5호선", "purple");
        lineDao.insert(purpleLine);
        lineDao.insert(greenLine);

        final List<String> expectedNames = List.of("5호선", LINE_NAME);
        final List<String> expectedColors = List.of("purple", LINE_COLOR);

        // when
        final ResponseEntity<List<LineResponse>> response = lineController.showLines();

        final List<String> actualNames = response
                .getBody()
                .stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());

        final List<String> actualColors = response
                .getBody()
                .stream()
                .map(LineResponse::getColor)
                .collect(Collectors.toList());

        // then
        assertThat(actualNames).isEqualTo(expectedNames);
        assertThat(actualColors).isEqualTo(expectedColors);
    }

    @Test
    @DisplayName("노선을 조회한다.")
    void ShowLine() {
        // given
        final LineRequest request = new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                gangnam.getId(),
                yeoksam.getId(),
                10,
                0
        );
        final Long id = lineController.createLine(request)
                .getBody()
                .getId();

        final List<String> expectedStationNames = List.of(
                gangnam.getName(),
                yeoksam.getName()
        );

        // when
        final ResponseEntity<LineResponse> response = lineController.showLine(id);

        final LineResponse actual = response.getBody();
        final List<String> actualStationNames = actual.getStations()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        // then
        assertThat(actual.getName()).isEqualTo(LINE_NAME);
        assertThat(actual.getColor()).isEqualTo(LINE_COLOR);
        assertThat(actualStationNames).isEqualTo(expectedStationNames);
    }

    @Test
    @DisplayName("존재하지 않는 노선을 조회하면 예외를 던진다.")
    void ShowLine_NotExist_ExceptionThrown() {
        // when, then
        assertThatThrownBy(() -> lineController.showLine(999L))
                .isInstanceOf(NoSuchLineException.class);
    }

    @Test
    @DisplayName("노선 정보를 수정한다.")
    void UpdateLine() {
        // given
        final Long id = lineDao.insert(greenLine)
                .orElseThrow()
                .getId();

        final LineRequest request = new LineRequest("5호선", "color", null, null, 10, 1000);

        // when
        final ResponseEntity<Void> response = lineController.updateLine(id, request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("수정하려는 노선 이름이 중복되면 예외를 던진다.")
    void UpdateLine_DuplicateName_ExceptionThrown() {
        // given
        final String name = "5호선";
        final String color = "color";
        lineDao.insert(new Line(name, color));

        final Long id = lineDao.insert(greenLine)
                .orElseThrow()
                .getId();

        final LineRequest request = new LineRequest(name, LINE_COLOR, null, null, 10, 0);

        // when, then
        assertThatThrownBy(() -> lineController.updateLine(id, request))
                .isInstanceOf(DuplicateLineException.class);
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void DeleteLine() {
        // given
        final Long id = lineDao.insert(greenLine)
                .orElseThrow()
                .getId();

        // when
        final ResponseEntity<Void> response = lineController.deleteLine(id);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}