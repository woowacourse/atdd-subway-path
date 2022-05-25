package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.service.ServiceTestFixture.deleteAllLine;
import static wooteco.subway.service.ServiceTestFixture.deleteAllSection;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.repository.JdbcLineRepository;
import wooteco.subway.repository.JdbcSectionRepository;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.dao.JdbcLineDao;
import wooteco.subway.repository.dao.JdbcSectionDao;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.service.dto.SectionServiceDeleteRequest;
import wooteco.subway.service.dto.SectionServiceRequest;

@JdbcTest
public class SectionServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionRepository sectionRepository;
    private LineRepository lineRepository;

    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        SectionDao sectionDao = new JdbcSectionDao(jdbcTemplate);
        LineDao lineDao = new JdbcLineDao(jdbcTemplate);
        sectionRepository = new JdbcSectionRepository(sectionDao, lineDao);
        lineRepository = new JdbcLineRepository(lineDao);

        sectionService = new SectionService(sectionRepository, lineRepository);

        deleteAllLine(lineRepository);
        deleteAllSection(sectionRepository);
    }

    @Test
    @DisplayName("상행점 구간을 저장한다. 정렬된 구간 : 2-3-4-5 -> 1-2-3-4-5")
    void saveFirstStation() {
        // given
        Long lineId = lineRepository.save(new Line("name", "color", 0));
        Line line = new Line(lineId, "name", "color", 0);
        sectionRepository.save(new Section(line, 2L, 3L, 3));
        sectionRepository.save(new Section(line, 3L, 4L, 4));
        sectionRepository.save(new Section(line, 4L, 5L, 5));

        // when
        sectionService.save(new SectionServiceRequest(1L, 2L, 2), lineId);
        List<Section> inputSections = sectionRepository.findByLineId(lineId);
        Sections sections = new Sections(inputSections);
        List<Long> stationIds = sections.sortedStationId();
        Long firstPointStationId = stationIds.get(0);

        Section firstSection = inputSections.stream()
            .filter(i -> i.mathUpStationId(firstPointStationId))
            .findAny()
            .get();

        // then
        assertAll(
            () -> assertThat(firstSection.getUpStationId()).isEqualTo(1L),
            () -> assertThat(firstSection.getDistance()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("하행점 구간을 저장한다. 정렬된 구간 : 1-2-3-4 -> 1-2-3-4-5")
    void saveLastStation() {
        // given
        Long lineId = lineRepository.save(new Line("name", "color", 0));
        Line line = new Line(lineId, "name", "color", 0);
        sectionRepository.save(new Section(line, 1L, 2L, 3));
        sectionRepository.save(new Section(line, 2L, 3L, 4));
        sectionRepository.save(new Section(line, 3L, 4L, 5));

        // when
        sectionService.save(new SectionServiceRequest(4L, 5L, 2), lineId);
        List<Section> inputSections = sectionRepository.findByLineId(lineId);
        Sections sections = new Sections(inputSections);

        List<Long> stationIds = sections.sortedStationId();
        Long lastPointStationId = stationIds.get(3);

        Section lastSection = inputSections.stream()
            .filter(i -> i.mathUpStationId(lastPointStationId))
            .findAny()
            .get();

        // then
        assertAll(
            () -> assertThat(lastSection.getDownStationId()).isEqualTo(5L),
            () -> assertThat(lastSection.getDistance()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("중간 지점 구간을 저장한다. 정렬된 구간 : 1-3구간에서 1-2 구간을 추가하는 경우")
    void saveMiddleStation1() {
        // given
        Long lineId = lineRepository.save(new Line("name", "color", 0));
        Line line = new Line(lineId, "name", "color", 0);
        sectionRepository.save(new Section(line, 1L, 3L, 3));

        // when
        sectionService.save(new SectionServiceRequest(1L, 2L, 2), lineId);
        List<Section> inputSections = sectionRepository.findByLineId(lineId);
        Sections sections = new Sections(inputSections);
        List<Long> stationIds = sections.sortedStationId();

        Section section1 = inputSections.stream()
            .filter(i -> i.mathUpStationId(stationIds.get(0)))
            .findAny()
            .get();

        Section section2 = inputSections.stream()
            .filter(i -> i.mathUpStationId(stationIds.get(1)))
            .findAny()
            .get();

        // then
        assertAll(
            () -> assertThat(section1.getDistance()).isEqualTo(2),
            () -> assertThat(section2.getDistance()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("중간 지점 구간을 저장한다. 정렬된 구간 : 1-3구간에서 2-3 구간을 추가하는 경우")
    void saveMiddleStation2() {
        // given
        Long lineId = lineRepository.save(new Line("name", "color", 0));
        Line line = new Line(lineId, "name", "color", 0);
        sectionRepository.save(new Section(line, 1L, 3L, 4));

        // when
        sectionService.save(new SectionServiceRequest(2L, 3L, 3), lineId);
        List<Section> inputSections = sectionRepository.findByLineId(lineId);

        Sections sections = new Sections(inputSections);
        List<Long> stationIds = sections.sortedStationId();

        Section section1 = inputSections.stream()
            .filter(i -> i.mathUpStationId(stationIds.get(0)))
            .findAny()
            .get();

        Section section2 = inputSections.stream()
            .filter(i -> i.mathUpStationId(stationIds.get(1)))
            .findAny()
            .get();

        // then
        assertAll(
            () -> assertThat(section1.getDistance()).isEqualTo(1),
            () -> assertThat(section2.getDistance()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("생성할 중간 지점 구간의 길이가 기존 구간의 길이보다 길거나 같은 경우 예외가 발생한다.")
    void validateMiddleStationDistance() {
        // given
        Long lineId = lineRepository.save(new Line("name", "color", 0));
        Line line = new Line(lineId, "name", "color", 0);
        sectionRepository.save(new Section(line, 2L, 3L, 3));
        sectionRepository.save(new Section(line, 3L, 4L, 4));
        sectionRepository.save(new Section(line, 4L, 5L, 5));

        assertThatThrownBy(() ->
            sectionService.save(new SectionServiceRequest(2L, 1L, 4), lineId))
            .hasMessage("등록할 구간의 길이가 기존 역 사이의 길이보다 길거나 같으면 안됩니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("중간 지점 구간 제거한다. 1-2-3-4 -> 1-2-4")
    void deleteMiddleSection() {
        // given
        Long lineId = lineRepository.save(new Line("name", "color", 0));
        Line line = new Line(lineId, "name", "color", 0);
        sectionRepository.save(new Section(line, 1L, 2L, 3));
        sectionRepository.save(new Section(line, 2L, 3L, 4));
        sectionRepository.save(new Section(line, 3L, 4L, 5));

        // when
        sectionService.removeSection(new SectionServiceDeleteRequest(lineId, 3L));
        List<Section> inputSections = sectionRepository.findByLineId(lineId);
        Sections sections = new Sections(inputSections);
        List<Long> result = sections.sortedStationId();

        Section section = inputSections.stream()
            .filter(i -> i.mathUpStationId(2L))
            .findAny()
            .get();

        // then
        List<Long> expected = List.of(1L, 2L, 4L);
        assertAll(
            () -> assertThat(result).containsExactlyInAnyOrderElementsOf(expected),
            () -> assertThat(section.getDistance()).isEqualTo(9)
        );
    }

    @Test
    @DisplayName("구간이 하나 밖에 없을 경우, 예외가 발생한다.")
    void validateDeleteEndStationSection() {
        // given
        Long lineId = lineRepository.save(new Line("name", "color", 0));
        Line line = new Line(lineId, "name", "color", 0);
        sectionRepository.save(new Section(line, 2L, 3L, 3));

        // then
        assertThatThrownBy(() ->
            sectionService.removeSection(new SectionServiceDeleteRequest(lineId, 2L)))
            .hasMessage("구간을 제거할 수 없는 상태입니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }
}
