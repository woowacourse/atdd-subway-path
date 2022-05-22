package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.acceptance.DBTest;
import wooteco.subway.domain.Line;

@JdbcTest
class LineDaoTest extends DBTest {

    private final LineDao lineDao;

    @Autowired
    public LineDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        this.lineDao = new LineDao(jdbcTemplate);
    }

    private final Line line = new Line("2호선", "green", 200);

    @DisplayName("노선을 저장한다.")
    @Test
    void save() {
        Line savedLine = lineDao.save(line);

        assertAll(
                () -> assertThat(savedLine.getName()).isEqualTo(line.getName()),
                () -> assertThat(savedLine.getColor()).isEqualTo(line.getColor()),
                () -> assertThat(savedLine.getExtraFare()).isEqualTo(line.getExtraFare())
        );
    }

    @DisplayName("같은 이름의 노선을 저장하는 경우 예외가 발생한다.")
    @Test
    void saveExistingName() {
        lineDao.save(line);

        assertThatThrownBy(() -> lineDao.save(line))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 노선을 조회한다.")
    @Test
    void findAll() {
        Line line1 = new Line("2호선", "green", 200);
        Line line2 = new Line("3호선", "orange", 300);
        Line line3 = new Line("8호선", "pink", 400);

        lineDao.save(line1);
        lineDao.save(line2);
        lineDao.save(line3);

        assertThat(lineDao.findAll().size()).isEqualTo(3);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findById() {
        Line savedLine = lineDao.save(line);

        Line foundLine = lineDao.findById(savedLine.getId());

        assertAll(
                () -> assertThat(foundLine.getId()).isEqualTo(savedLine.getId()),
                () -> assertThat(foundLine.getName()).isEqualTo(savedLine.getName()),
                () -> assertThat(foundLine.getColor()).isEqualTo(savedLine.getColor()),
                () -> assertThat(foundLine.getExtraFare()).isEqualTo(savedLine.getExtraFare())
        );
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
        Line savedLine = lineDao.save(line);
        Line updatingLine = new Line("3호선", "orange", 500);

        lineDao.updateById(savedLine.getId(), updatingLine);

        Line foundLine = lineDao.findById(savedLine.getId());

        assertAll(
                () -> assertThat(foundLine.getId()).isEqualTo(savedLine.getId()),
                () -> assertThat(foundLine.getName()).isEqualTo(updatingLine.getName()),
                () -> assertThat(foundLine.getColor()).isEqualTo(updatingLine.getColor()),
                () -> assertThat(foundLine.getExtraFare()).isEqualTo(updatingLine.getExtraFare())
        );
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteById() {
        Line savedLine = lineDao.save(line);

        lineDao.deleteById(savedLine.getId());

        assertThat(lineDao.findAll().size()).isZero();
    }
}
