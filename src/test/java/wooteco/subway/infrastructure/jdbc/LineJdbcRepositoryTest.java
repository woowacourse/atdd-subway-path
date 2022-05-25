package wooteco.subway.infrastructure.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.TestFixture.강남_역삼;
import static wooteco.subway.TestFixture.강남역_ID;
import static wooteco.subway.TestFixture.선릉_삼성;
import static wooteco.subway.TestFixture.역삼_선릉;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.infrastructure.jdbc.dao.LineDao;
import wooteco.subway.infrastructure.jdbc.dao.SectionDao;

@DisplayName("지하철노선 Repository")
@JdbcTest
class LineJdbcRepositoryTest {

    private static final List<Section> SECTIONS = List.of(강남_역삼, 역삼_선릉);
    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "green";
    private static final long LINE_EXTRA_FARE = 0L;
    private static final Line DEFAULT_LINE = new Line(SECTIONS, LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE);

    @Autowired
    private DataSource dataSource;
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        LineDao lineDao = new LineDao(dataSource);
        SectionDao sectionDao = new SectionDao(dataSource);
        this.lineRepository = new LineJdbcRepository(lineDao, sectionDao);
    }

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void save() {
        Line line = new Line(SECTIONS, LINE_NAME, LINE_COLOR, LINE_EXTRA_FARE);
        Line actual = lineRepository.save(line);

        assertAll(
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("id", "sections")
                        .isEqualTo(line),
                () -> assertThat(actual.getSections()).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(line.getSections())
        );
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @ParameterizedTest
    @ValueSource(ints = {3})
    void getAll(int expected) {
        IntStream.rangeClosed(1, expected)
                .mapToObj(id -> new Line(SECTIONS, "호선" + id, "색상" + id, LINE_EXTRA_FARE))
                .forEach(lineRepository::save);

        List<Line> actual = lineRepository.getAll();
        assertThat(actual).hasSize(expected);
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getById() {
        Line expected = new Line(SECTIONS, "신분당선", "color", LINE_EXTRA_FARE);
        long lineId = lineRepository.save(expected).getId();

        Line actual = lineRepository.getById(lineId);
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id", "sections")
                .isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 지하철노선을 조회한다.")
    @Test
    void findNonExistentLine() {
        assertThatThrownBy(() -> lineRepository.getById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("지하철노선을 찾을 수 없습니다.");
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void update() {
        Line expected = lineRepository.save(new Line(SECTIONS, "신분당선", "color1", LINE_EXTRA_FARE));
        expected.update("분당선", "color2", 300);

        Line actual = lineRepository.update(expected);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 지하철노선을 수정한다.")
    @Test
    void updateNonExistentLine() {
        assertThatThrownBy(() -> lineRepository.update(new Line(1L, SECTIONS, "신분당선", "color", LINE_EXTRA_FARE)))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("지하철노선을 찾을 수 없습니다.");
    }

    @DisplayName("구간 목록을 수정한다.")
    @Test
    void updateSections() {
        Line line = lineRepository.save(DEFAULT_LINE);
        List<Section> expected = List.of(강남_역삼, 역삼_선릉, 선릉_삼성);
        lineRepository.updateSections(
                new Line(line.getId(), expected, line.getName(), line.getColor(), LINE_EXTRA_FARE));

        List<Section> actual = lineRepository.getById(line.getId()).getSections();
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void remove() {
        Line line = lineRepository.save(DEFAULT_LINE);
        lineRepository.remove(line.getId());

        List<Line> actual = lineRepository.getAll();
        assertThat(actual).isEmpty();
    }

    @DisplayName("이름에 해당하는 노선이 존재하는지 확인한다.")
    @Test
    void existsByName() {
        lineRepository.save(DEFAULT_LINE);

        boolean actual = lineRepository.existsByName(DEFAULT_LINE.getName());
        assertThat(actual).isTrue();
    }

    @DisplayName("색상에 해당하는 노선이 존재하는지 확인한다.")
    @Test
    void existsByColor() {
        lineRepository.save(DEFAULT_LINE);

        boolean actual = lineRepository.existsByColor(DEFAULT_LINE.getColor());
        assertThat(actual).isTrue();
    }

    @DisplayName("역을 참조하고 있는 구간이 존재하는지 확인한다.")
    @Test
    void existsSectionByStationId() {
        lineRepository.save(DEFAULT_LINE);

        boolean actual = lineRepository.existsSectionByStationId(강남역_ID);
        assertThat(actual).isTrue();
    }
}