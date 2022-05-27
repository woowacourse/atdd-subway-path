package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.domain.line.Line;

@JdbcTest
class LineDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private Long lineId1;
    private Long lineId2;

    private LineDao lineDao;

    @BeforeEach
    void init() {
        lineDao = new LineDao(jdbcTemplate);
        lineId1 = lineDao.save(new Line("신분당선", "red", 500));
        lineId2 = lineDao.save(new Line("2호선", "green", 0));
    }

    @DisplayName("노선 저장")
    @Test
    void save() {
        // given
        Line line = new Line("분당선", "yellow", 0);

        // when
        Long id = lineDao.save(line);

        // then
        assertThat(id).isEqualTo(lineId2 + 1);
    }

    @DisplayName("노선 이름이 존재하는지 확인")
    @Test
    void existsByName() {
        // given
        String name = "신분당선";

        // when
        boolean result = lineDao.existsByName(name);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("특정 id를 제외하고 노선 이름이 존재하는지 확인")
    @Test
    void existsByNameExceptWithId() {
        // given
        String name = "신분당선";

        // when
        boolean result = lineDao.existsByNameExceptWithId(name, lineId1);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("해당 id 존재하는지 확인")
    @Test
    void existsById() {
        // given

        // when
        boolean result = lineDao.existsById(lineId1);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("노선 id로 검색")
    @Test
    void findById() {
        // given
        Line expected = new Line(lineId1, "신분당선", "red", 500);

        // when
        Optional<Line> line = lineDao.findById(lineId1);

        // then
        assertThat(line.isPresent()).isTrue();
        assertThat(line.get()).isEqualTo(expected);
    }

    @DisplayName("노선 전체 조회")
    @Test
    void findAll() {
        // given

        // when
        List<Line> lines = lineDao.findAll();

        // then
        assertThat(lines.size()).isEqualTo(2);
    }

    @DisplayName("id 목록으로 노선 조회")
    @Test
    void findByIds() {
        // given
        List<Long> ids = List.of(lineId1);

        // when
        List<Line> lines = lineDao.findByIds(ids);

        // then
        assertThat(lines.size()).isEqualTo(1);
    }

    @DisplayName("노선 정보 수정")
    @Test
    void update() {
        // given
        Line lineRequest = new Line(lineId1, "신분당선", "pink", 0);

        // when
        lineDao.update(lineRequest);

        // then
        Optional<Line> line = lineDao.findById(lineId1);

        assertThat(line.isPresent()).isTrue();
        assertThat(line.get()).extracting(Line::getName, Line::getColor)
                .contains(lineRequest.getName(), lineRequest.getColor());
    }

    @DisplayName("노선 삭제")
    @Test
    void deleteById() {
        // given

        // when
        lineDao.deleteById(lineId1);

        // then
        assertThat(lineDao.findById(lineId1)).isEqualTo(Optional.empty());
    }
}
