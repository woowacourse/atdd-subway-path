package wooteco.subway.ui;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;

@SpringBootTest
@Transactional
public class ControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    protected StationDao stationDao;

    @Autowired
    protected SectionDao sectionDao;

    @Autowired
    protected LineDao lineDao;

    @AfterEach
    public void reset() {
        jdbcTemplate.execute("DELETE FROM section");
        jdbcTemplate.execute("DELETE FROM station");
        jdbcTemplate.execute("DELETE FROM line");
        jdbcTemplate.execute("ALTER TABLE station ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE line ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE section ALTER COLUMN id RESTART WITH 1");
    }
}
