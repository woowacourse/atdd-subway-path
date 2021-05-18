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
}