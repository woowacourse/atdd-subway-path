package wooteco.subway.fixture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.station.Station;

@Component
public class DatabaseFixtureUtils {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public void saveStations(Station... stations) {
        String sql = "INSERT INTO station(name) VALUES (?)";
        for (Station station : stations) {
            jdbcTemplate.update(sql, station.getName());
        }
    }

    public void saveLine(String name, String color) {
        String sql = "INSERT INTO line(name, color) VALUES (?, ?)";
        jdbcTemplate.update(sql, name, color);
    }

    public void saveLine(String name, String color, int extraFare) {
        String sql = "INSERT INTO line(name, color, extra_fare) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, name, color, extraFare);
    }

    public void saveSection(Long lineId, Station upStation, Station downStation) {
        saveSection(lineId, upStation, downStation, 10);
    }

    public void saveSection(Long lineId, Station upStation, Station downStation, int distance) {
        String sql = "INSERT INTO section(line_id, up_station_id, down_station_id, distance) "
                + "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, lineId, upStation.getId(), downStation.getId(), distance);
    }
}
