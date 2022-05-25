package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.repository.dao.JdbcLineDao;
import wooteco.subway.repository.entity.LineEntity;

@JdbcTest
class JdbcLineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcLineDao jdbcLineDao;

    @BeforeEach
    void setUp() {
        jdbcLineDao = new JdbcLineDao(jdbcTemplate);

        List<LineEntity> lineEntities = jdbcLineDao.findAll();
        List<Long> lineIds = lineEntities.stream()
            .map(LineEntity::getId)
            .collect(Collectors.toList());

        for (Long lineId : lineIds) {
            jdbcLineDao.deleteById(lineId);
        }
    }

    @Test
    void save() {
        // given
        Line line = new Line("1호선", "bg-red-600", 0);

        // when
        Long savedId = jdbcLineDao.save(line);
        LineEntity line1 = jdbcLineDao.findById(savedId).get();

        // then
        assertThat(line.getName()).isEqualTo(line1.getName());
    }

    @Test
    void validateDuplication() {
        // given
        Line line1 = new Line("1호선", "bg-red-600", 0);
        Line line2 = new Line("1호선", "bg-red-600", 0);

        // when
        jdbcLineDao.save(line1);

        // then
        assertThatThrownBy(() -> jdbcLineDao.save(line2))
            .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void findAll() {
        // given
        Line line1 = new Line("1호선", "bg-red-600", 0);
        Line line2 = new Line("2호선", "bg-green-600", 0);

        // when
        jdbcLineDao.save(line1);
        jdbcLineDao.save(line2);

        // then
        List<String> names = jdbcLineDao.findAll()
            .stream()
            .map(LineEntity::getName)
            .collect(Collectors.toList());

        assertThat(names)
            .hasSize(2)
            .contains(line1.getName(), line2.getName());
    }

    @Test
    void delete() {
        // given
        Line line = new Line("1호선", "bg-red-600", 0);
        Long savedId = jdbcLineDao.save(line);

        // when
        jdbcLineDao.deleteById(savedId);

        // then
        List<Long> lineIds = jdbcLineDao.findAll()
            .stream()
            .map(LineEntity::getId)
            .collect(Collectors.toList());

        assertThat(lineIds)
            .hasSize(0)
            .doesNotContain(savedId);
    }

    @Test
    void update() {
        // given
        Line originLine = new Line("1호선", "bg-red-600", 0);
        Long savedId = jdbcLineDao.save(originLine);

        // when
        Line newline = new Line(savedId, "2호선", "bg-green-600", 0);
        jdbcLineDao.updateById(newline);
        LineEntity line = jdbcLineDao.findById(savedId).get();

        // then
        assertThat(line.getName()).isEqualTo(newline.getName());
    }
}
