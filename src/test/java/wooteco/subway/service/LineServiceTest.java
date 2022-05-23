package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.LineUpdateRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.BothUpAndDownStationDoNotExistException;
import wooteco.subway.exception.BothUpAndDownStationExistException;
import wooteco.subway.exception.CanNotInsertSectionException;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.exception.NotFoundLineException;
import wooteco.subway.exception.OnlyOneSectionException;

@Transactional
@JdbcTest
class LineServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineService lineService;

    private LineResponse lineResponse;
    private LineDao lineDao;

    private StationResponse stationResponse1;
    private StationResponse stationResponse2;
    private StationResponse stationResponse3;
    private StationResponse stationResponse4;
    private StationResponse stationResponse5;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        lineService = new LineService(lineDao, new StationDao(jdbcTemplate), new SectionDao(jdbcTemplate));
        StationService stationService = new StationService(new StationDao(jdbcTemplate));

        stationResponse1 = stationService.createStation(new StationRequest("선릉역"));
        stationResponse2 = stationService.createStation(new StationRequest("삼성역"));
        stationResponse3 = stationService.createStation(new StationRequest("종합운동장역"));
        stationResponse4 = stationService.createStation(new StationRequest("잠실새내역"));
        stationResponse5 = stationService.createStation(new StationRequest("잠실역"));

        lineResponse = lineService.createLine(
                new LineRequest("2호선", "bg-green-600", 500, stationResponse2.getId(), stationResponse4.getId(), 10));
    }

    // TODO: stations 도 함께 LineResponse에 포함해야함
    @DisplayName("이름, 색상, 상행선, 하행선, 길이를 전달받아 새로운 노선을 등록한다.")
    @Test
    void createLine() {
        // given
        String name = "3호선";
        String color = "bg-green-600";
        Integer extraFare = 500;
        Long upStationId = stationResponse1.getId();
        Long downStationId = stationResponse2.getId();
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
        Long upStationId = stationResponse1.getId();
        Long downStationId = stationResponse2.getId();
        Integer distance = 10;

        LineRequest lineRequest = new LineRequest(name, color, extraFare, upStationId, downStationId, distance);

        // when & then
        assertThatThrownBy(() -> lineService.createLine(lineRequest))
                .isInstanceOf(DuplicateNameException.class);
    }

    @DisplayName("등록된 모든 노선을 반환한다.")
    @Test
    void getAllLines() {
        // when
        List<String> actualNames = lineService.getAllLines()
                .stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());

        List<String> actualColors = lineService.getAllLines()
                .stream()
                .map(LineResponse::getColor)
                .collect(Collectors.toList());

        List<String> expectedNames = List.of("2호선");
        List<String> expectedColors = List.of("bg-green-600");

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
        LineRequest lineRequest = new LineRequest(lineName, lineColor, extraFare, stationResponse1.getId(),
                stationResponse2.getId(), 10);

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
        LineRequest lineRequest = new LineRequest(lineName, lineColor, extraFare, stationResponse1.getId(),
                stationResponse2.getId(),
                10);
        LineResponse createdLine = lineService.createLine(lineRequest);

        // when
        String newLineName = "새로운 호선";
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
        LineRequest lineRequest = new LineRequest(lineName, lineColor, extraFare, stationResponse1.getId(),
                stationResponse2.getId(),
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

    @DisplayName("노선에 새로운 구간을 상행선 방향으로 추가")
    @Test
    void createSection_upStation() {
        // given
        Long lineId = lineResponse.getId();

        System.out.println(lineResponse);

        Long upStationId = stationResponse1.getId();
        Long downStationId = stationResponse2.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 10);

        // when
        lineService.createSection(lineId, sectionRequest);

        // then
        Line line = lineDao.findById(lineId).get();
        boolean isCreatedSectionExisting = line.getSections().getValue()
                .stream()
                .anyMatch(section -> section.getUpStation().getId().equals(sectionRequest.getUpStationId())
                        && section.getDownStation().getId().equals(sectionRequest.getDownStationId()));

        assertThat(isCreatedSectionExisting).isTrue();
    }

    @DisplayName("노선에 새로운 구간을 하행선 방향으로 추가")
    @Test
    void createSection_downStation() {
        // given
        Long lineId = lineResponse.getId();
        Long upStationId = stationResponse4.getId();
        Long downStationId = stationResponse5.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 10);

        // when
        lineService.createSection(lineId, sectionRequest);

        // then
        Sections sections = lineDao.findById(lineId).get().getSections();
        boolean isCreatedSectionExisting = sections.getValue()
                .stream()
                .anyMatch(section -> section.getUpStation().getId().equals(sectionRequest.getUpStationId())
                        && section.getDownStation().getId().equals(sectionRequest.getDownStationId()));

        assertThat(isCreatedSectionExisting).isTrue();
    }

    @DisplayName("노선에 새로운 구간을 이미 존재하는 구간 사이에 삽입")
    @Test
    void createSection_inserting() {
        // given
        Long lineId = lineResponse.getId();
        Long upStationId = stationResponse2.getId();
        Long downStationId = stationResponse3.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 5);

        // when
        lineService.createSection(lineId, sectionRequest);

        // then
        Sections sections = lineDao.findById(lineId).get().getSections();
        boolean isCreatedSectionExisting = sections.getValue()
                .stream()
                .anyMatch(section -> section.getUpStation().getId().equals(sectionRequest.getUpStationId())
                        && section.getDownStation().getId().equals(sectionRequest.getDownStationId()));

        assertThat(isCreatedSectionExisting).isTrue();
    }

    @DisplayName("삽입하려는 구간이 기존 구간보다 길이가 같거나 긴 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {10, 11, 100})
    void createSection_throwsExceptionOnInsertingIfInsertedSectionIsLongerThanBaseSection(int distance) {
        // given
        Long lineId = lineResponse.getId();
        Long upStationId = stationResponse2.getId();
        Long downStationId = stationResponse3.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);

        // when & then
        assertThatThrownBy(() -> lineService.createSection(lineId, sectionRequest))
                .isInstanceOf(CanNotInsertSectionException.class);
    }

    @DisplayName("추가하려는 구간의 모든 역이 이미 구간 목록에 모두 존재할 경우 예외가 발생한다")
    @Test
    void createSection_throwsExceptionIfBothUpAndDownStationAreAlreadyExisting() {
        // given
        Long lineId = lineResponse.getId();
        Long upStationId = stationResponse2.getId();
        Long downStationId = stationResponse4.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 10);

        // when & then
        assertThatThrownBy(() -> lineService.createSection(lineId, sectionRequest))
                .isInstanceOf(BothUpAndDownStationExistException.class);
    }

    @DisplayName("추가하려는 구간의 모든 역이 구간 목록에 모두 존재하지 않을 경우 예외가 발생한다")
    @Test
    void createSection_throwsExceptionIfBothUpAndDownStationAreNotExisting() {
        // given
        Long lineId = lineResponse.getId();
        Long upStationId = stationResponse1.getId();
        Long downStationId = stationResponse5.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 10);

        // when & then
        assertThatThrownBy(() -> lineService.createSection(lineId, sectionRequest))
                .isInstanceOf(BothUpAndDownStationDoNotExistException.class);
    }

    @DisplayName("상행종점역 제거")
    @Test
    void deleteStationById_upStation() {
        // given
        Long lineId = lineResponse.getId();
        Long upStationId = stationResponse1.getId();
        Long downStationId = stationResponse2.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 10);

        lineService.createSection(lineId, sectionRequest);

        // when
        lineService.deleteStationById(lineId, upStationId);

        // then
        int actual = lineDao.findById(lineId).get().getSections().getValue().size();
        assertThat(actual).isOne();
    }

    @DisplayName("하행종점역 제거")
    @Test
    void deleteStationById_downStation() {
        // given
        Long lineId = lineResponse.getId();
        Long upStationId = stationResponse4.getId();
        Long downStationId = stationResponse5.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 10);

        lineService.createSection(lineId, sectionRequest);

        // when
        lineService.deleteStationById(lineId, downStationId);

        // then
        int actual = lineDao.findById(lineId).get().getSections().getValue().size();
        assertThat(actual).isOne();
    }

    @DisplayName("중간역 제거")
    @Test
    void deleteStationById_betweenStation() {
        // given
        Long lineId = lineResponse.getId();
        Long upStationId = stationResponse2.getId();
        Long downStationId = stationResponse3.getId();
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 5);

        lineService.createSection(lineId, sectionRequest);

        // when
        lineService.deleteStationById(lineId, downStationId);

        // then
        int actual = lineDao.findById(lineId).get().getSections().getValue().size();
        assertThat(actual).isOne();
    }

    @DisplayName("구간이 단 하나인 구간 목록에서 역을 제거하면 예외가 발생한다")
    @Test
    void deleteStationById_throwsExceptionIfSectionsSizeIsOne() {
        // given
        Long lineId = lineResponse.getId();
        Long deleteStationId = stationResponse2.getId();

        // when & then
        assertThatThrownBy(() -> lineService.deleteStationById(lineId, deleteStationId))
                .isInstanceOf(OnlyOneSectionException.class);
    }
}
