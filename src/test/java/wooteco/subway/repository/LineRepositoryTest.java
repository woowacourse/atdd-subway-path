package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("delete from line");
    }

    @Test
    @DisplayName("노선을 생성한다.")
    void create() {
        Line line = lineRepository.save(new Line("1호선", "blue", 0));

        assertThat(line.getId()).isNotNull();
    }

    @Test
    @DisplayName("노선을 id로 조회한다")
    void findById() {
        Line line = lineRepository.save(new Line("1호선", "blue", 0));

        assertThat(lineRepository.findById(line.getId())).isEqualTo(line);
    }

    @Test
    @DisplayName("저장된 노선들을 조회한다.")
    void findAll() {
        lineRepository.save(new Line("1호선", "blue", 0));
        lineRepository.save(new Line("2호선", "green", 0));
        lineRepository.save(new Line("3호선", "orange", 0));

        assertThat(lineRepository.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("id로 노선을 삭제한다.")
    void deleteById() {
        Line line = lineRepository.save(new Line("1호선", "blue", 0));

        lineRepository.deleteById(line.getId());

        assertThat(lineRepository.findAll()).hasSize(0);
    }

}
