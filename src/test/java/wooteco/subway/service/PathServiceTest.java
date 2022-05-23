package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;

@JdbcTest
public class PathServiceTest {

    private PathService pathService;
    private Long firstStationId;
    private Long secondStationId;
    private Long thirdStationId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.pathService = new PathService(
                new StationDao(jdbcTemplate),
                new SectionDao(jdbcTemplate),
                new LineDao(jdbcTemplate)
        );

        this.firstStationId = insertStation("테스트1역");
        this.secondStationId = insertStation("테스트2역");
        this.thirdStationId = insertStation("테스트3역");

        var lineId = insertLine("테스트호선", "테스트색", 1000);

        insertSection(lineId, firstStationId, secondStationId, 2);
        insertSection(lineId, secondStationId, thirdStationId, 2);
    }

    private Long insertLine(String name, String color, int extraFare) {
        var sql = "INSERT INTO line (name, color, extraFare) values(?, ?, ?)";

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, name);
            statement.setString(2, color);
            statement.setInt(3, extraFare);
            return statement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private Long insertStation(String name) {
        var keyHolder = new GeneratedKeyHolder();
        var sql = "INSERT INTO station (name) values(?)";

        jdbcTemplate.update(con -> {
            var statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, name);
            return statement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private void insertSection(Long lineId, Long firstStationId, Long secondStationId, int distance) {
        var sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) values(?, ?, ?, ?)";

        jdbcTemplate.update(sql, lineId, firstStationId, secondStationId, distance);
    }

    @Test
    void findPath() {
        var pathResponse = pathService.findPath(firstStationId, thirdStationId, -1);

        assertAll(
                () -> assertThat(pathResponse.getStations().size()).isEqualTo(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(4),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2250)
        );
    }
}
