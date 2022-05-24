package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.line.JdbcLineDao;
import wooteco.subway.domain.line.Line;

@JdbcTest
class LineDaoTest {

    private static final Line LINE = new Line("신분당선", "bg-red-600", 900);

    private JdbcLineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        lineDao = new JdbcLineDao(jdbcTemplate);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void save() {
        lineDao.save(LINE);

        Integer count = jdbcTemplate.queryForObject("select count(*) from LINE", Integer.class);

        assertThat(count).isEqualTo(1);
    }

    @DisplayName("해당 ID의 지하철 노선이 있다면 true를 반환한다.")
    @Test
    void existLineById() {
        long lineId = lineDao.save(LINE);

        assertThat(lineDao.existLineById(lineId)).isTrue();
    }

    @DisplayName("지하철 노선의 전체 목록을 조회한다.")
    @Test
    void findAll() {
        lineDao.save(LINE);
        lineDao.save(new Line("다른분당선", "bg-green-600", 600));

        List<Line> lines = lineDao.findAll();

        assertThat(lines).hasSize(2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findById() {
        long lineId = lineDao.save(LINE);

        Line line = lineDao.findById(lineId);

        assertThat(line).isNotNull();
    }

    @DisplayName("id 리스트로 지하철 노선 전체를 조회한다.")
    @Test
    void findByIds() {
        Line line2 = new Line("다른분당선", "bg-red-700", 0);
        List<Long> ids = new ArrayList<>();
        ids.add(lineDao.save(LINE));
        ids.add(lineDao.save(line2));

        List<Line> lines = lineDao.findByIds(ids);

        assertAll(
                () -> assertThat(lines.stream().map(Line::getId).collect(Collectors.toList())).hasSameElementsAs(ids),
                () -> assertThat(lines.stream().map(Line::getName).collect(Collectors.toList()))
                        .contains("신분당선", "다른분당선")
        );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void update() {
        long lineId = lineDao.save(LINE);
        Line updatedLine = new Line(lineId, "다른분당선", "bg-red-600", 900);

        lineDao.update(updatedLine);

        assertThat(lineDao.findById(lineId).getName()).isEqualTo("다른분당선");
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void delete() {
        long lineId = lineDao.save(LINE);

        lineDao.delete(lineId);

        assertThat(lineDao.existLineById(lineId)).isFalse();
    }
}
