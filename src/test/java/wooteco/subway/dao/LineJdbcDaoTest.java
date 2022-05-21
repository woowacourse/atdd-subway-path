package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;

public class LineJdbcDaoTest extends DaoImplTest{

    private LineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        lineDao = new LineJdbcDao(jdbcTemplate);
    }

    @DisplayName("노선정보를 저장한다.")
    @Test
    void save() {
        Line line = new Line("분당선", "green", 900);
        Line newLine = lineDao.save(line);

        assertThat(newLine.getName()).isEqualTo("분당선");
        assertThat(newLine.getColor()).isEqualTo("green");
    }

    @DisplayName("노선정보들을 가져온다.")
    @Test
    void findAll() {
        Line line = new Line("분당선", "green", 900);
        Line nextLine = new Line("신분당선", "green", 900);
        lineDao.save(line);
        lineDao.save(nextLine);

        List<Line> lines = lineDao.findAll();

        assertThat(lines.size()).isEqualTo(2);
    }

    @DisplayName("노선 정보를 삭제한다.")
    @Test
    void delete() {
        Line line = new Line("4호선", "blue", 900);
        Line newLine = lineDao.save(line);

        assertThat(lineDao.delete(newLine.getId())).isOne();
    }

    @DisplayName("노선 정보를 조회한다.")
    @Test
    void find() {
        Line line = new Line("5호선", "blue", 900);
        Line newLine = lineDao.save(line);

        assertThat(lineDao.findById(newLine.getId()).getName()).isEqualTo("5호선");
        assertThat(lineDao.findById(newLine.getId()).getColor()).isEqualTo("blue");
    }

    @DisplayName("노선 정보를 변경한다.")
    @Test
    void update() {
        Line line = new Line(1L,"7호선", "blue", 900);
        Line newLine = new Line(1L, "8호선", "red", 900);

        lineDao.save(line);

        assertThat(lineDao.update(newLine)).isOne();
    }
}
