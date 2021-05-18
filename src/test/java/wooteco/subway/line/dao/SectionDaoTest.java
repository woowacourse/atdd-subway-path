package wooteco.subway.line.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

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