package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.repository.dao.JdbcSectionDao;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.entity.SectionEntity;

@JdbcTest
public class JdbcSectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new JdbcSectionDao(jdbcTemplate);
    }

    @Test
    void save() {
        Line line = new Line(1L, "name", "color", 0);
        Section section = new Section(line, 2L, 3L, 10);
        assertDoesNotThrow(() -> sectionDao.save(section));
    }

    @Test
    void delete() {
        // given
        Line line = new Line(1L, "name", "color", 0);
        Long savedId = sectionDao.save(new Section(line, 2L, 3L, 3));

        // when
        sectionDao.deleteById(savedId);

        // then
        List<SectionEntity> sections = sectionDao.findByLineId(1L);
        assertThat(sections).hasSize(0);
    }
}
