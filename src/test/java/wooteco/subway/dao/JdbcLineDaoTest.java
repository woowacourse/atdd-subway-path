package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Line;

@JdbcTest
@Sql("/lineInitSchema.sql")
class JdbcLineDaoTest {

    private final LineDao lineDao;

    @Autowired
    public JdbcLineDaoTest(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.lineDao = new JdbcLineDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void LineCreateTest() {
        final Long lineId = lineDao.save(Line.createWithoutId("신분당선", "red", 0));

        assertThat(lineDao.findById(lineId).get())
                .extracting("name", "color")
                .containsExactly("신분당선", "red");
    }

    @Test
    @DisplayName("지하철 노선을 단건 조회한다.")
    void LineReadTest() {
        final Long lineId = lineDao.save(Line.createWithoutId("1호선", "dark-blue", 0));

        final Line line = lineDao.findById(lineId).get();

        assertThat(line)
                .extracting("name", "color")
                .containsExactly("1호선", "dark-blue");
    }

    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void LineDeleteTest() {
        final Long lineId = lineDao.save(Line.createWithoutId("신분당선", "red", 0));

        lineDao.deleteById(lineId);

        assertThat(lineDao.findById(lineId)).isEmpty();
    }

    @Test
    @DisplayName("지하철 노선을 전체 조회한다.")
    void findAll() {
        final Long lineId = lineDao.save(Line.createWithoutId("신분당선", "red", 0));
        final List<Line> lines = lineDao.findAll();

        assertThat(lines).hasSize(1)
                .extracting("name", "color")
                .containsExactly(tuple("신분당선", "red"));

        lineDao.deleteById(lineId);
    }

    @Test
    @DisplayName("지하철 노선을 업데이트한다.")
    void update() {
        final Long lineId = lineDao.save(Line.createWithoutId("신분당선", "red", 0));

        lineDao.update(lineId, Line.createWithoutId("분당선", "yellow", 0));

        final Line newLine = lineDao.findById(lineId).get();

        assertThat(newLine)
                .extracting("name", "color")
                .containsExactly("분당선", "yellow");

        lineDao.deleteById(lineId);
    }
}
