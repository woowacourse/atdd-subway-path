package wooteco.subway.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.SubwayFixtures.강남에서_역삼_구간;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.vo.LineColor;
import wooteco.subway.domain.vo.LineId;
import wooteco.subway.domain.vo.LineName;
import wooteco.subway.infra.dao.LineDao;
import wooteco.subway.infra.dao.SectionDao;
import wooteco.subway.infra.repository.JdbcLineRepository;
import wooteco.subway.infra.repository.JdbcSectionRepository;
import wooteco.subway.infra.repository.LineRepository;
import wooteco.subway.infra.repository.SectionRepository;

@DisplayName("Line 레포지토리")
@Sql("/truncate.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
@JdbcTest
class LineRepositoryTest {

    private static final Sections SECTIONS = new Sections(List.of(강남에서_역삼_구간));

    private final LineRepository lineRepository;

    public LineRepositoryTest(JdbcTemplate jdbcTemplate, DataSource dataSource,
                              NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        final SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource, namedParameterJdbcTemplate);
        final LineDao lineDao = new LineDao(jdbcTemplate, dataSource, namedParameterJdbcTemplate);
        final SectionRepository jdbcSectionRepository = new JdbcSectionRepository(sectionDao);

        this.lineRepository = new JdbcLineRepository(lineDao, jdbcSectionRepository);
    }

    @Test
    @DisplayName("Line 저장")
    void save() {
        // given
        final Line line = new Line(LineName.from("2호선"), LineColor.from("bg-600-green"), SECTIONS);

        // when
        final Line saved = lineRepository.save(line);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo(line.getName()),
                () -> assertThat(saved.getColor()).isEqualTo(line.getColor())
        );
    }


    @Test
    @DisplayName("Line 단 건 조회")
    void findById() {
        // given
        final Line line = new Line(LineName.from("2호선"), LineColor.from("bg-600-green"), SECTIONS);
        final Line saved = lineRepository.save(line);

        // when
        final Optional<Line> foundLine = lineRepository.findById(saved.getId());
        final Line found = foundLine.orElseThrow(NoSuchElementException::new);

        // then
        assertAll(
                () -> assertThat(saved.getName()).isEqualTo(line.getName()),
                () -> assertThat(saved.getColor()).isEqualTo(line.getColor()),
                () -> assertThat(found.getName()).isEqualTo(saved.getName()),
                () -> assertThat(found.getColor()).isEqualTo(saved.getColor())
        );
    }

    @Test
    @DisplayName("Line 업데이트")
    void updateById() {
        // given
        final Line line = new Line(LineName.from("2호선"), LineColor.from("bg-600-green"), SECTIONS);
        final Line saved = lineRepository.save(line);

        // when
        final Line newLine = new Line(LineId.from(saved.getId()), LineName.from("3호선"), LineColor.from("bg-700-blue"));
        final long affectedRow = lineRepository.update(newLine);
        final Optional<Line> updated = lineRepository.findById(saved.getId());

        // then
        assertAll(
                () -> assertThat(affectedRow).isOne(),
                () -> assertThat(updated).isPresent(),
                () -> assertThat(updated.get().getName()).isEqualTo(newLine.getName()),
                () -> assertThat(updated.get().getColor()).isEqualTo(newLine.getColor())
        );
    }

    @Test
    @DisplayName("Line 단 건 삭제")
    void deleteById() {
        // given
        final Line line = new Line(LineName.from("2호선"), LineColor.from("bg-600-green"), SECTIONS);
        final Line saved = lineRepository.save(line);
        final Long id = saved.getId();

        // when
        final Optional<Line> before = lineRepository.findById(id);
        lineRepository.deleteById(id);
        final Optional<Line> after = lineRepository.findById(id);

        // then
        assertAll(
                () -> assertThat(before).isPresent(),
                () -> assertThat(after).isNotPresent()
        );
    }
}
