package wooteco.subway.dao;

import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;

@Repository
public class StationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StationDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Station save(Station station) {
        String sql = "INSERT INTO station (name) VALUES (:name)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource("name", station.getName());

        jdbcTemplate.update(sql, params, keyHolder);
        long stationId = keyHolder.getKey().longValue();
        return new Station(stationId, station.getName());
    }

    public List<Station> findAll() {
        String sql = "SELECT * FROM station";

        return jdbcTemplate.query(sql, new MapSqlParameterSource(), getRowMapper());
    }

    public List<Station> findAllByLineId(long lineId) {
        String sql = "SELECT * FROM station WHERE"
                + " id IN(SELECT st.id FROM section AS se INNER JOIN station AS st"
                + " ON se.up_station_id = st.id"
                + " WHERE se.line_id = :lineId) OR"
                + " id IN(SELECT st.id FROM section AS se INNER JOIN station AS st"
                + " ON se.down_station_id = st.id"
                + " WHERE se.line_id = :lineId)";

        SqlParameterSource params = new MapSqlParameterSource("lineId", lineId);

        return jdbcTemplate.query(sql, params, getRowMapper());
    }

    public Station findById(long id) {
        String sql = "SELECT id, name FROM station WHERE id=:id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, params, getRowMapper());
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM station WHERE id=:id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, params);
    }

    private RowMapper<Station> getRowMapper() {
        return (rs, rowNum) -> new Station(rs.getLong("id"), rs.getString("name"));
    }
}
