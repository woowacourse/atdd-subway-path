package wooteco.subway.line.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.line.domain.Section;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql(scripts = {"classpath:schema.sql", "classpath:dummy.sql"})
class SectionDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private final SectionDao sectionDao;

    @Autowired
    public SectionDaoTest(JdbcTemplate jdbcTemplate, @Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @DisplayName("주어진 역들 중 하나라도 가지고 있는 구간들을 찾는다.")
    @Test
    void findByStationIds() {
        List<Long> stationIds = Arrays.asList(1L, 2L);
        List<Section> sections = sectionDao.findByStationIds(stationIds);

        assertThat(sections).hasSize(3);
    }
}