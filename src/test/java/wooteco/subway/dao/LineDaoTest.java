package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.domain.Line;

@JdbcTest
public class LineDaoTest {

    private static final Line _2호선 = new Line("2호선", "green", 0);
    private static final Line _3호선 = new Line("3호선", "orange", 0);
    private static final Line _8호선 = new Line("8호선", "pink", 0);

    private final LineDao lineDao;

    @Autowired
    public LineDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    @DisplayName("노선을 저장한다.")
    @Test
    void save() {
        final Line line = lineDao.save(_2호선);

        assertThat(line.getName()).isEqualTo(_2호선.getName());
    }

    @DisplayName("같은 이름의 노선을 저장하는 경우 예외가 발생한다.")
    @Test
    void saveExistingName() {
        lineDao.save(_2호선);

        assertThatThrownBy(() -> lineDao.save(_2호선))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 노선을 조회한다.")
    @Test
    void findAll() {
        lineDao.save(_2호선);
        lineDao.save(_3호선);
        lineDao.save(_8호선);

        assertThat(lineDao.findAll().size()).isEqualTo(3);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findById() {
        final Line line = lineDao.save(_2호선);

        final Line foundLine = lineDao.findById(line.getId());

        assertThat(foundLine.getName()).isEqualTo(line.getName());
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회할 경우 예외가 발생한다.")
    @Test
    void findNotExistingLine() {
        assertThatThrownBy(() -> lineDao.findById(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void update() {
        final Line line = lineDao.save(_2호선);

        lineDao.updateById(line.getId(), _3호선);

        assertThat(lineDao.findById(line.getId()).getName()).isEqualTo(_3호선.getName());
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteById() {
        final Line line = lineDao.save(_2호선);

        lineDao.deleteById(line.getId());

        assertThat(lineDao.findAll().size()).isZero();
    }
}
