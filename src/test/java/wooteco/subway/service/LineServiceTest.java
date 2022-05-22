package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.LineUpdateRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.exception.NotFoundLineException;

@Transactional
@JdbcTest
class LineServiceTest {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineService lineService;

    private StationResponse createdStation1;
    private StationResponse createdStation2;

    @BeforeEach
    void setUp() {
        lineService = new LineService(new LineDao(jdbcTemplate), new StationDao(jdbcTemplate),
                new SectionDao(jdbcTemplate));
        StationService stationService = new StationService(new StationDao(jdbcTemplate));

        createdStation1 = stationService.createStation(new StationRequest("선릉역"));
        createdStation2 = stationService.createStation(new StationRequest("잠실역"));
    }

    // TODO: stations 도 함께 LineResponse에 포함해야함
    @DisplayName("이름, 색상, 상행선, 하행선, 길이를 전달받아 새로운 노선을 등록한다.")
    @Test
    void createLine() {
        // given
        String name = "2호선";
        String color = "bg-green-600";
        Integer extraFare = 500;
        Long upStationId = createdStation1.getId();
        Long downStationId = createdStation2.getId();
        Integer distance = 10;

        LineRequest lineRequest = new LineRequest(name, color, extraFare, upStationId, downStationId, distance);

        // when
        LineResponse actual = lineService.createLine(lineRequest);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(name),
                () -> assertThat(actual.getColor()).isEqualTo(color)
        );
    }

    @DisplayName("중복된 이름의 노선을 등록할 경우 예외를 발생한다.")
    @Test
    void createLine_throwsExceptionWithDuplicateName() {
        // given
        String name = "2호선";
        String color = "bg-green-600";
        Integer extraFare = 500;
        Long upStationId = createdStation1.getId();
        Long downStationId = createdStation2.getId();
        Integer distance = 10;

        LineRequest lineRequest = new LineRequest(name, color, extraFare, upStationId, downStationId, distance);
        lineService.createLine(lineRequest);

        // when & then
        assertThatThrownBy(() -> lineService.createLine(lineRequest))
                .isInstanceOf(DuplicateNameException.class);
    }

    @DisplayName("등록된 모든 노선을 반환한다.")
    @Test
    void getAllLines() {
        // given
        String lineName1 = "1호선";
        String lineColor1 = "bg-blue-600";
        String lineName2 = "2호선";
        String lineColor2 = "bg-green-600";
        Integer extraFare = 500;
        LineRequest lineRequest1 = new LineRequest(lineName1, lineColor1, extraFare, createdStation1.getId(),
                createdStation2.getId(), 10);
        LineRequest lineRequest2 = new LineRequest(lineName2, lineColor2, extraFare, createdStation1.getId(),
                createdStation2.getId(), 10);

        lineService.createLine(lineRequest1);
        lineService.createLine(lineRequest2);

        // when
        List<String> actualNames = lineService.getAllLines()
                .stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());

        List<String> actualColors = lineService.getAllLines()
                .stream()
                .map(LineResponse::getColor)
                .collect(Collectors.toList());

        List<String> expectedNames = List.of("1호선", "2호선");
        List<String> expectedColors = List.of("bg-blue-600", "bg-green-600");

        // then
        assertAll(
                () -> assertThat(actualNames).containsAll(expectedNames),
                () -> assertThat(actualColors).containsAll(expectedColors)
        );
    }

    @DisplayName("노선 ID로 개별 노선을 찾아 반환한다.")
    @Test
    void getLineById() {
        // given
        String lineName = "1호선";
        String lineColor = "bg-blue-600";
        Integer extraFare = 500;
        LineRequest lineRequest = new LineRequest(lineName, lineColor, extraFare, createdStation1.getId(),
                createdStation2.getId(), 10);

        LineResponse createdLine = lineService.createLine(lineRequest);

        // when
        LineResponse actual = lineService.getLineById(createdLine.getId());

        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(createdLine.getId()),
                () -> assertThat(actual.getName()).isEqualTo(createdLine.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(createdLine.getColor())
        );
    }

    @DisplayName("노선 ID로 노선을 업데이트 한다.")
    @Test
    void updateLine() {
        // given
        String lineName = "1호선";
        String lineColor = "bg-blue-600";
        Integer extraFare = 500;
        LineRequest lineRequest = new LineRequest(lineName, lineColor, extraFare, createdStation1.getId(),
                createdStation2.getId(),
                10);
        LineResponse createdLine = lineService.createLine(lineRequest);

        // when
        String newLineName = "2호선";
        String newLineColor = "bg-red-600";
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(newLineName, newLineColor);
        lineService.update(createdLine.getId(), lineUpdateRequest);

        // then
        LineResponse actual = lineService.getLineById(createdLine.getId());
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(lineUpdateRequest.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(lineUpdateRequest.getColor())
        );
    }

    @DisplayName("수정하려는 노선 ID가 존재하지 않을 경우 예외를 발생한다.")
    @Test
    void update_throwsExceptionIfLineIdIsNotExisting() {
        // given
        String newLineName = "2호선";
        String newLineColor = "bg-red-600";
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest(newLineName, newLineColor);

        // when & then
        assertThatThrownBy(() -> lineService.update(10L, lineUpdateRequest))
                .isInstanceOf(NotFoundLineException.class);
    }

    @DisplayName("등록된 노선을 삭제한다.")
    @Test
    void delete() {
        // given
        String lineName = "1호선";
        String lineColor = "bg-blue-600";
        Integer extraFare = 500;
        LineRequest lineRequest = new LineRequest(lineName, lineColor, extraFare, createdStation1.getId(),
                createdStation2.getId(),
                10);
        LineResponse createdLine = lineService.createLine(lineRequest);

        // when
        lineService.delete(createdLine.getId());

        // then
        boolean isNotExistLine = lineService.getAllLines()
                .stream()
                .filter(lineResponse -> lineResponse.getId().equals(createdLine.getId()))
                .findAny()
                .isEmpty();

        assertThat(isNotExistLine).isTrue();
    }

    @DisplayName("삭제하려는 노선 ID가 존재하지 않을 경우 예외를 발생한다.")
    @Test
    void delete_throwsExceptionIfLineIdIsNotExisting() {
        assertThatThrownBy(() -> lineService.delete(1L))
                .isInstanceOf(NotFoundLineException.class);
    }
}
