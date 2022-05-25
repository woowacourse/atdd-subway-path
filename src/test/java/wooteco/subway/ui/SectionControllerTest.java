package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.ui.dto.SectionRequest;

class SectionControllerTest extends ControllerTest {

    @Autowired
    private SectionController sectionController;

    @DisplayName("상행 종점이 같은 구간을 추가한다.")
    @Test
    void createUpperMiddleSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station3.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station2.getId(), 5);
        ResponseEntity<Void> response = sectionController.createSection(line.getId(), sectionRequest);

        // then
        HttpStatus statusCode = response.getStatusCode();
        List<Section> sections = sectionDao.findAllByLineId(line.getId()).getSections();

        assertSectionConnection(station1, station2, station3, statusCode, sections);
    }

    @DisplayName("하행 종점이 같은 구간을 추가한다.")
    @Test
    void createLowerMiddleSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station3.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 5);
        ResponseEntity<Void> response = sectionController.createSection(line.getId(), sectionRequest);

        // then
        HttpStatus statusCode = response.getStatusCode();
        List<Section> sections = sectionDao.findAllByLineId(line.getId()).getSections();

        assertSectionConnection(station1, station2, station3, statusCode, sections);
    }

    @DisplayName("상행 종점을 연장한다.")
    @Test
    void createUpperSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station2.getId(), 5);
        ResponseEntity<Void> response = sectionController.createSection(line.getId(), sectionRequest);

        // then
        HttpStatus statusCode = response.getStatusCode();
        Sections sections = sectionDao.findAllByLineId(line.getId());
        List<Long> stationIds = sections.getSortedStationIdsInSingleLine();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(sections.getSections()).hasSize(2),
                () -> assertThat(sections.getSections()
                        .stream()
                        .map(Section::getDistance))
                        .containsExactly(10, 5),
                () -> assertThat(stationIds).hasSize(3),
                () -> assertThat(stationIds.get(0)).isEqualTo(station1.getId()),
                () -> assertThat(stationIds.get(1)).isEqualTo(station2.getId()),
                () -> assertThat(stationIds.get(2)).isEqualTo(station3.getId())
        );
    }

    @DisplayName("하행 종점을 연장한다.")
    @Test
    void createLowerSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 5);
        ResponseEntity<Void> response = sectionController.createSection(line.getId(), sectionRequest);

        // then
        HttpStatus statusCode = response.getStatusCode();
        Sections sections = sectionDao.findAllByLineId(line.getId());
        List<Long> stationIds = sections.getSortedStationIdsInSingleLine();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(sections.getSections()).hasSize(2),
                () -> assertThat(sections.getSections()
                        .stream()
                        .map(Section::getDistance))
                        .containsExactly(10, 5),
                () -> assertThat(stationIds).hasSize(3),
                () -> assertThat(stationIds.get(0)).isEqualTo(station1.getId()),
                () -> assertThat(stationIds.get(1)).isEqualTo(station2.getId()),
                () -> assertThat(stationIds.get(2)).isEqualTo(station3.getId())
        );
    }

    @DisplayName("길이가 기존 구간의 길이를 초과한 구간을 추가할 경우 예외가 발생한다.")
    @Test
    void createLongerMiddleSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station3.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);

        assertThatThrownBy(() -> sectionController.createSection(line.getId(), sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 구간의 길이보다 작아야합니다.");
    }

    @DisplayName("상행과 하행 종점이 모두 포함되지 않는 구간을 추가할 경우 예외가 발생한다.")
    @Test
    void createNotMatchingSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Station station4 = stationDao.save(new Station("성수역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station3.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station4.getId(), 10);

        assertThatThrownBy(() -> sectionController.createSection(line.getId(), sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행 종점과 하행 종점 중 하나의 종점만 포함되어야 합니다.");
    }

    @DisplayName("추가할 상행과 하행 종점이 이미 구간에 존재할 경우 예외가 발생한다.")
    @Test
    void createAllMatchingSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station1.getId(), 10);

        assertThatThrownBy(() -> sectionController.createSection(line.getId(), sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행 종점과 하행 종점 중 하나의 종점만 포함되어야 합니다.");
    }

    @DisplayName("상행과 하행 종점이 동일한 구간을 추가할 경우 예외가 발생한다.")
    @Test
    void createSectionWhereUpStationsIsSameAsDownStation() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 0);

        assertThatThrownBy(() -> sectionController.createSection(line.getId(), sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역간의 거리는 1 이상이어야 합니다.");
    }

    @DisplayName("추가하려는 구간의 길이가 1 미만일 경우 예외가 발생한다.")
    @Test
    void createSectionWithInvalidDistance() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));

        // when
        SectionRequest sectionRequest = new SectionRequest(station1.getId(), station1.getId(), 10);

        assertThatThrownBy(() -> sectionController.createSection(line.getId(), sectionRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행 종점과 하행 종점 중 하나의 종점만 포함되어야 합니다.");
    }

    @DisplayName("상행 종점을 제거한다.")
    @Test
    void deleteUpperSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 10));

        // when
        ResponseEntity<Void> response = sectionController.deleteSection(line.getId(), station1.getId());

        // then
        HttpStatus statusCode = response.getStatusCode();
        List<Section> sections = sectionDao.findAllByLineId(line.getId()).getSections();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(sections).hasSize(1),
                () -> assertThat(sections.get(0).getUpStationId()).isEqualTo(station2.getId()),
                () -> assertThat(sections.get(0).getDownStationId()).isEqualTo(station3.getId()),
                () -> assertThat(sections.get(0).getDistance()).isEqualTo(10)
        );
    }

    @DisplayName("중간역을 제거한다.")
    @Test
    void deleteMiddleSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 10));

        // when
        ResponseEntity<Void> response = sectionController.deleteSection(line.getId(), station2.getId());

        // then
        HttpStatus statusCode = response.getStatusCode();
        List<Section> sections = sectionDao.findAllByLineId(line.getId()).getSections();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(sections).hasSize(1),
                () -> assertThat(sections.get(0).getUpStationId()).isEqualTo(station1.getId()),
                () -> assertThat(sections.get(0).getDownStationId()).isEqualTo(station3.getId()),
                () -> assertThat(sections.get(0).getDistance()).isEqualTo(20)
        );
    }

    @DisplayName("하행 종점을 제거한다.")
    @Test
    void deleteLowerSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 10));

        // when
        ResponseEntity<Void> response = sectionController.deleteSection(line.getId(), station3.getId());

        // then
        HttpStatus statusCode = response.getStatusCode();
        List<Section> sections = sectionDao.findAllByLineId(line.getId()).getSections();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(sections).hasSize(1),
                () -> assertThat(sections.get(0).getUpStationId()).isEqualTo(station1.getId()),
                () -> assertThat(sections.get(0).getDownStationId()).isEqualTo(station2.getId()),
                () -> assertThat(sections.get(0).getDistance()).isEqualTo(10)
        );
    }

    @DisplayName("구간이 하나뿐인 노선의 구간을 삭제할 경우 예외가 발생한다.")
    @Test
    void deleteMinimumSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));

        // when
        assertThatThrownBy(() -> sectionController.deleteSection(line.getId(), station2.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간이 하나뿐이라 삭제할 수 없습니다.");
    }

    @DisplayName("노선에 포함되지 않는 구간을 삭제할 경우 예외가 발생한다.")
    @Test
    void deleteNotExistingSection() {
        // given
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("역삼역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Station station4 = stationDao.save(new Station("성수역"));
        Line line = lineDao.save(new Line("2호선", "green", 0));
        sectionDao.save(new Section(line.getId(), station1.getId(), station2.getId(), 10));
        sectionDao.save(new Section(line.getId(), station2.getId(), station3.getId(), 10));

        // when
        assertThatThrownBy(() -> sectionController.deleteSection(line.getId(), station4.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    private void assertSectionConnection(Station station1, Station station2, Station station3, HttpStatus statusCode,
                                         List<Section> sections) {
        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(sections).hasSize(2),
                () -> assertThat(sections.get(0).getUpStationId()).isEqualTo(station1.getId()),
                () -> assertThat(sections.get(0).getDownStationId()).isEqualTo(station2.getId()),
                () -> assertThat(sections.get(0).getDistance()).isEqualTo(5),
                () -> assertThat(sections.get(1).getUpStationId()).isEqualTo(station2.getId()),
                () -> assertThat(sections.get(1).getDownStationId()).isEqualTo(station3.getId()),
                () -> assertThat(sections.get(1).getDistance()).isEqualTo(5)
        );
    }
}