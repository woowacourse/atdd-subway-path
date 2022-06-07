package wooteco.subway.dao;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Section> sectionRowMapper = (resultSet, rowNum) ->
            new Section(
                    resultSet.getLong("id"),
                    resultSet.getLong("line_id"),
                    new Station(resultSet.getLong("up_station_id"), resultSet.getString("up_station_name")),
                    new Station(resultSet.getLong("down_station_id"), resultSet.getString("down_station_name")),
                    resultSet.getInt("distance")
            );

    public void insert(Section section) {
        simpleJdbcInsert.execute(
                Map.of("line_id", section.getLineId(), "up_station_id", section.getUpStation().getId(),
                        "down_station_id", section.getDownStation().getId(), "distance", section.getDistance()));
    }
    
    public List<Section> findAll() {
        String sql = "select sc.id, sc.line_id, sc.distance "
                + ", up.id as up_station_id, up.name as up_station_name "
                + ", down.id as down_station_id, down.name as down_station_name "
                + "from SECTION sc, STATION up, STATION down "
                + "where sc.up_station_id = up.id and sc.down_station_id = down.id ";
        return jdbcTemplate.query(sql, sectionRowMapper);
    }

    public List<Section> findByLineId(Long lineId) {
        final String sql = "select sc.id, sc.line_id, sc.distance "
                + ", up.id as up_station_id, up.name as up_station_name "
                + ", down.id as down_station_id, down.name as down_station_name "
                + "from SECTION sc, STATION up, STATION down "
                + "where sc.up_station_id = up.id and sc.down_station_id = down.id "
                + "and sc.line_id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM SECTION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public int deleteByLineIdAndStationId(Long lineId, Long stationId) {
        final String sql = "DELETE FROM SECTION WHERE line_id = ? AND (up_station_id = ? OR down_station_id = ?)";
        return jdbcTemplate.update(sql, lineId, stationId, stationId);
    }

    public void deleteByLineId(Long lineId) {
        final String sql = "DELETE FROM SECTION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public boolean existsByStationId(Long stationId) {
        final String sql = "SELECT EXISTS (SELECT * FROM SECTION WHERE (up_station_id = ? OR down_station_id = ?))";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, stationId, stationId));
    }
}
