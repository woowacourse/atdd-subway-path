package wooteco.subway.fixture;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.entity.LineEntity;

@Component
public class DatabaseFixtureUtils {

    @Autowired
    protected NamedParameterJdbcTemplate jdbcTemplate;

    public void saveLines(LineEntity... lines) {
        String sql = "INSERT INTO line(name, color, extra_fare) VALUES (:name, :color, :extraFare)";
        List<LineEntity> lineList = Arrays.stream(lines)
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(lineList));
    }

    public void saveStations(Station... stations) {
        final String sql = "INSERT INTO station(name) VALUES (:name)";
        List<Station> stationList = Arrays.stream(stations)
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(stationList));
    }

    public void saveSections(Section... sections) {
        String sql = "INSERT INTO section(line_id, up_station_id, down_station_id, distance) "
                + "VALUES (:lineId, :upStationId, :downStationId, :distance)";
        List<Section> sectionList = Arrays.stream(sections)
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(sectionList));
    }
}
