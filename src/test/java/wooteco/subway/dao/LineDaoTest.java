package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Line;

@JdbcTest
@Sql("classpath:line.sql")
public class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);

        lineDao.save(new Line("신분당선", "bg-red-600", 0));
        lineDao.save(new Line("짱분당선", "bg-blue-600", 0));
        lineDao.save(new Line("구분당선", "bg-green-600", 0));
    }

    @Test
    @DisplayName("정상적으로 저장된 경우를 테스트한다.")
    void saveTest() {
        final Line newLine = lineDao.save(new Line("라쿤선", "bg-black-600", 0));
        assertAll(
                () -> assertThat(newLine.getName()).isEqualTo("라쿤선"),
                () -> assertThat(newLine.getColor()).isEqualTo("bg-black-600")
        );
    }

    @Test
    @DisplayName("정상적으로 전체 조회되는 경우를 테스트한다.")
    void findAllTest() {
        assertThat(lineDao.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("존재하지 않는 id를 조회하는 경우 예외를 발생시킨다.")
    void findExceptionTest() {
        Optional<Line> line = lineDao.findById(9999L);

        assertThat(line.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("정상적으로 특정 조회하는 경우를 테스트한다.")
    void findByIdTest() {
        final Line newLine = lineDao.save(new Line("라쿤선", "bg-black-600", 0));
        final Line line = lineDao.findById(newLine.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 지하철 노선입니다."));
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("라쿤선"),
                () -> assertThat(line.getColor()).isEqualTo("bg-black-600")
        );
    }

    @Test
    @DisplayName("정상적으로 수정되는 경우를 테스트한다.")
    void updateTest() {
        final Line newLine = lineDao.save(new Line("짱구선", "bg-white-600", 0));
        lineDao.update(newLine.getId(), new Line("38선", "bg-rainbow-600", 0));
        Line line = lineDao.findById(newLine.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 지하철 노선입니다."));

        assertAll(
                () -> assertThat(line.getName()).isEqualTo("38선"),
                () -> assertThat(line.getColor()).isEqualTo("bg-rainbow-600")
        );
    }

    @Test
    @DisplayName("정상적으로 제거되는 경우를 테스트한다.")
    void deleteTest() {
        final Line newLine = lineDao.save(new Line("짱구선", "bg-white-600", 0));
        int actual = lineDao.deleteById(newLine.getId());
        assertThat(actual).isEqualTo(1);
    }
}
