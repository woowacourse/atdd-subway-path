package wooteco.subway.service;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Station;

public class ServiceTest {

    protected static final Station 강남역 = new Station("강남역");
    protected static final Station 선릉역 = new Station("선릉역");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void reset() {
        jdbcTemplate.execute("DELETE FROM section");
        jdbcTemplate.execute("DELETE FROM station");
        jdbcTemplate.execute("DELETE FROM line");
    }
}
