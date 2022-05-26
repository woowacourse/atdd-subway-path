package wooteco.subway.dao;

import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

@Repository
public class SectionDao {

    private static final RowMapper<Section> ROW_MAPPER = (resultSet, rowNum) -> {
        Long lineId = resultSet.getLong("line_id");
        Station upStation = new Station(
                resultSet.getLong("up_station_id"),
                resultSet.getString("up_station_name"));
        Station downStation = new Station(
                resultSet.getLong("down_station_id"),
                resultSet.getString("down_station_name"));
        int distance = resultSet.getInt("distance");
        return new Section(lineId, upStation, downStation, distance);
    };

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SectionDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Section> findAll() {
        final String sql = "SELECT A.line_id AS line_id, A.distance AS distance, "
                + "B.id AS up_station_id, B.name AS up_station_name, "
                + "C.id AS down_station_id, C.name AS down_station_name "
                + "FROM section A, station B, station C "
                + "WHERE A.up_station_id = B.id AND A.down_station_id = C.id";

        return jdbcTemplate.query(sql, new EmptySqlParameterSource(), ROW_MAPPER);
    }

    public List<Section> findAllByLineId(Long lineId) {
        final String sql = "SELECT A.line_id AS line_id, A.distance AS distance, "
                + "B.id AS up_station_id, B.name AS up_station_name, "
                + "C.id AS down_station_id, C.name AS down_station_name "
                + "FROM section A, station B, station C "
                + "WHERE A.up_station_id = B.id AND A.down_station_id = C.id "
                + "AND A.line_id = :lineId";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("lineId", lineId);

        return jdbcTemplate.query(sql, paramSource, ROW_MAPPER);
    }

    public List<Section> findAllByStationId(Long stationId) {
        final String sql = "SELECT DISTINCT A.line_id AS line_id, A.distance AS distance, "
                + "B.id AS up_station_id, B.name AS up_station_name, "
                + "C.id AS down_station_id, C.name AS down_station_name "
                + "FROM section A, station B, station C "
                + "WHERE A.up_station_id = B.id AND A.down_station_id = C.id "
                + "AND (A.up_station_id = :stationId OR A.down_station_id = :stationId)";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("stationId", stationId);

        return jdbcTemplate.query(sql, paramSource, ROW_MAPPER);
    }

    public void save(List<Section> sectionEntities) {
        final String sql = "INSERT INTO section(line_id, up_station_id, down_station_id, distance) "
                + "VALUES(:lineId, :upStationId, :downStationId, :distance)";

        jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(sectionEntities));
    }

    public void delete(List<Section> sectionEntities) {
        final String sql = "DELETE FROM section WHERE line_id = :lineId "
                + "AND (up_station_id = :upStationId OR down_station_id = :downStationId)";

        jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(sectionEntities));
    }

    public void deleteAllByLineId(Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = :lineId ";
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("lineId", lineId);

        jdbcTemplate.update(sql, paramSource);
    }
}
