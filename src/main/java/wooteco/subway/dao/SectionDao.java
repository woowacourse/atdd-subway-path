package wooteco.subway.dao;

import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

@Repository
public class SectionDao {

    private static final String selectSql = "select sc.id, sc.line_id, sc.distance "
            + ", up.id as up_station_id, up.name as up_station_name "
            + ", down.id as down_station_id, down.name as down_station_name "
            + "from SECTION sc, STATION up, STATION down "
            + "where sc.up_station_id = up.id and sc.down_station_id = down.id ";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SectionDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Section> eventRowMapper = (resultSet, rowNum)
            -> new Section(resultSet.getLong("id")
            , resultSet.getLong("line_id")
            , new Station(resultSet.getLong("up_station_id"), resultSet.getString("up_station_name"))
            , new Station(resultSet.getLong("down_station_id"), resultSet.getString("down_station_name"))
            , resultSet.getInt("distance")
    );

    public List<Section> findAll() {
        return jdbcTemplate.query(selectSql, eventRowMapper);
    }

    public List<Section> findByLineId(Long id) {
        String sql = selectSql + "and sc.line_id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, source, eventRowMapper);
    }

    public List<Section> findByStationId(Long id) {
        String sql = selectSql + "and sc.up_station_id = :id or sc.down_station_id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, source, eventRowMapper);
    }

    public List<Section> findByLineIdAndStationId(Long lineId, Long stationId) {
        String sql =
                selectSql
                        + "and sc.line_id = :lineId and (sc.up_station_id = :stationId or sc.down_station_id = :stationId)";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("lineId", lineId)
                .addValue("stationId", stationId);
        return jdbcTemplate.query(sql, source, eventRowMapper);
    }

    public Long save(Section section) {
        String sql = "insert into SECTION (line_id, up_station_id, down_station_id, distance) "
                + "values (:lineId, :upStationId, :downStationId, :distance)";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("lineId", section.getLineId())
                .addValue("upStationId", section.getUpStation().getId())
                .addValue("downStationId", section.getDownStation().getId())
                .addValue("distance", section.getDistance());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, source, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void update(Section section) {
        String sql = "update SECTION set down_station_id = :downStationId, up_station_id = :upStationId, distance = :distance where id = :id";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", section.getId())
                .addValue("upStationId", section.getUpStation().getId())
                .addValue("downStationId", section.getDownStation().getId())
                .addValue("distance", section.getDistance());

        jdbcTemplate.update(sql, source);
    }

    public void deleteByLineId(Long lineId) {
        String sql = "delete from SECTION where line_id = :lineId";
        SqlParameterSource source = new MapSqlParameterSource("lineId", lineId);
        jdbcTemplate.update(sql, source);
    }

    public void deleteById(Long id) {
        String sql = "delete from SECTION where id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, source);
    }
}
