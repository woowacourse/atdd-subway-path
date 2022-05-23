package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Line;

@JdbcTest
@Sql("/lineInitSchema.sql")
class JdbcLineDaoTest {

    private final LineDao lineDao;

    @Autowired
    public JdbcLineDaoTest(JdbcTemplate jdbcTemplate,
                           DataSource dataSource,
                           NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.lineDao = new JdbcLineDao(jdbcTemplate, dataSource, namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void LineCreateTest() {
        Long lineId = lineDao.save(new Line("신분당선", "red", 0));

        assertThat(lineDao.findById(lineId))
            .extracting("name", "color")
            .containsExactly("신분당선", "red");
    }

    @Test
    @DisplayName("지하철 노선을 단건 조회한다.")
    void LineReadTest() {
        Long lineId = lineDao.save(new Line("1호선", "dark-blue", 0));

        Line line = lineDao.findById(lineId);

        assertThat(line)
            .extracting("name", "color")
            .containsExactly("1호선", "dark-blue");
    }

    @Test
    @DisplayName("지하철 노선을 삭제한다.")
    void LineDeleteTest() {
        Long lineId = lineDao.save(new Line("신분당선", "red", 0));

        lineDao.deleteById(lineId);

        assertThatThrownBy(() -> lineDao.findById(lineId))
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("지하철 노선을 전체 조회한다.")
    void findAll() {
        Long lineId = lineDao.save(new Line("신분당선", "red", 0));
        List<Line> lines = lineDao.findAll();

        assertThat(lines).hasSize(1)
            .extracting("name", "color")
            .containsExactly(tuple("신분당선", "red"));

        lineDao.deleteById(lineId);
    }

    @Test
    @DisplayName("지하철 노선을 업데이트한다.")
    void update() {
        Long lineId = lineDao.save(new Line("신분당선", "red", 0));

        lineDao.update(lineId, new Line("분당선", "yellow", 0));

        Line newLine = lineDao.findById(lineId);

        assertThat(newLine)
            .extracting("name", "color")
            .containsExactly("분당선", "yellow");

        lineDao.deleteById(lineId);
    }

    @Test
    @DisplayName("입력으로 주어진 라인들 중 추가 요금이 가장 높은 라인의 추가 요금을 가져온다.")
    void getMaxExtraFareTest() {
        List<Long> lineIds = new ArrayList<>();
        lineIds.add(lineDao.save(new Line("신분당선", "red", 0)));
        lineIds.add(lineDao.save(new Line("1호선", "blue", 2000)));
        lineIds.add(lineDao.save(new Line("2호선", "yellow", 800)));

        int maxExtraFare = lineDao.findMaxExtraFare(lineIds);

        assertThat(maxExtraFare).isEqualTo(2000);
    }
}
