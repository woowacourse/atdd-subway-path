package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.exception.IllegalInputException;
import wooteco.subway.exception.line.NoSuchLineException;

class SectionControllerTest extends ControllerTest {

    @Autowired
    private SectionController sectionController;

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

        greenLine = lineDao.insert(new Line("2호선", "green")).orElseThrow();
        sectionDao.insert(new Section(greenLine, gangnam, seolleung, new Distance(10)));
    }

    @Test
    @DisplayName("존재하지 않는 노선에 구간을 추가하면 예외를 던진다.")
    void CreateSection_NotExistLine_ExceptionThrown() {
        // given
        final SectionRequest request = new SectionRequest(gangnam.getId(), yeoksam.getId(), 10);

        // when, then
        assertThatThrownBy(() -> sectionController.createSection(999L, request))
                .isInstanceOf(NoSuchLineException.class);
    }

    @Test
    @DisplayName("노선에 하나도 포함되지 않은 역들을 구간으로 추가하면 예외를 던진다.")
    void CreateSection_NotIncludeStation_ExceptionThrown() {
        // given
        final Long lineId = greenLine.getId();
        final SectionRequest request = new SectionRequest(yeoksam.getId(), samsung.getId(), 10);

        // when, then
        assertThatThrownBy(() -> sectionController.createSection(lineId, request))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("상행역과 하행역 중 하나의 역만 노선에 포함되어 있어야 합니다.");
    }

    @Test
    @DisplayName("모두 노선에 포함된 상행 역과 하행 역을 구간으로 추가하면 예외를 던진다.")
    void CreateSection_BothInclude_ExceptionThrown() {
        // given
        final Long lineId = greenLine.getId();
        final SectionRequest request = new SectionRequest(seolleung.getId(), gangnam.getId(), 10);

        // when, then
        assertThatThrownBy(() -> sectionController.createSection(lineId, request))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("상행역과 하행역 중 하나의 역만 노선에 포함되어 있어야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("기존 구간 사이에 기존 구간의 길이보다 길거나 같은 구간을 추가하면 예외를 던진다.")
    @ValueSource(ints = {10, 11})
    void CreateSection_EqualOrLongerThenExistSection_ExceptionThrown(final int distance) {
        // given
        final Long lineId = greenLine.getId();
        final SectionRequest request = new SectionRequest(gangnam.getId(), yeoksam.getId(), distance);

        // when, then
        assertThatThrownBy(() -> sectionController.createSection(lineId, request))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("기존 구간의 길이 보다 작지 않습니다.");
    }

    @Test
    @DisplayName("노선을 추가한다.")
    void CreateSection() {
        // given
        final SectionRequest request = new SectionRequest(yeoksam.getId(), seolleung.getId(), 9);

        // when
        final ResponseEntity<Void> response = sectionController.createSection(greenLine.getId(), request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("구간이 하나인 노선의 구간을 삭제하면 예외를 던진다.")
    void DeleteSection_LastOne_ExceptionThrown() {
        // given
        final Long lineId = greenLine.getId();
        final Long stationId = gangnam.getId();

        // when, then
        assertThatThrownBy(() -> sectionController.deleteSection(lineId, stationId))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("구간을 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void DeleteSection() {
        // given
        sectionDao.insert(new Section(greenLine, yeoksam, seolleung, new Distance(4)));

        final Long lineId = greenLine.getId();
        final Long stationId = yeoksam.getId();

        // when
        final ResponseEntity<Void> response = sectionController.deleteSection(lineId, stationId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}