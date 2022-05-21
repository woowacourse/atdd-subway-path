package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.line.Line;
import wooteco.subway.exception.line.LineNotFoundException;

@JdbcTest
@Sql("classpath:truncate.sql")
class LineDaoTest {

    @Autowired
    private DataSource dataSource;

    private LineDao lineDao;
    private Line line;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(dataSource);
        line = lineDao.insert(new Line("신분당선", "red"));
    }

    @DisplayName("이름값을 받아 해당 이름값을 가진 노선이 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"2호선, red, true", "신분당선, blue, true", "신분당선, red, true", "2호선, blue, false"})
    void existByNameOrColor(String name, String color, boolean expected) {
        boolean actual = lineDao.existByNameOrColor(new Line(name, color));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("id에 해당하는 노선의 정보를 가져온다.")
    @Test
    void getById() {
        Line actual = lineDao.getById(line.getId());

        assertThat(actual.getId()).isEqualTo(1L);
    }

    @DisplayName("id에 해당하는 노선이 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void getById_exception() {
        assertThatThrownBy(() -> lineDao.getById(-1L))
                .isInstanceOf(LineNotFoundException.class);
    }

    @DisplayName("모든 노선의 정보를 가져온다.")
    @Test
    void findAll() {
        lineDao.insert(new Line("8호선", "pink"));

        List<Line> actual = lineDao.findAll();

        assertThat(actual).hasSize(2);
    }

    @DisplayName("id에 해당하는 노선의 정보를 바꾼다.")
    @Test
    void edit() {
        Line updatingLine = new Line(line.getId(), "7호선", "darkGreen");
        lineDao.edit(updatingLine);

        Line updated = lineDao.getById(line.getId());

        assertThat(updated.getName()).isEqualTo("7호선");
        assertThat(updated.getColor()).isEqualTo("darkGreen");
    }

    @DisplayName("id에 해당하는 노선을 제거한다.")
    @Test
    void deleteById() {
        lineDao.deleteById(line.getId());

        assertThat(lineDao.findAll()).isEmpty();
    }
}
