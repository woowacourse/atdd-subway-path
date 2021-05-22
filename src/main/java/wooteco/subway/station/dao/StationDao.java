package wooteco.subway.station.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Repository
public class StationDao {
    private NamedParameterJdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Station> findByIds(List<Long> ids) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("ids", ids);
        String query = "select id, name from station where id in (:ids)";
        return jdbcTemplate.query(query, mapSqlParameterSource, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = :id";
        jdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

    public Station findById(Long id) {
        String sql = "select * from STATION where id = :id";
        return jdbcTemplate.queryForObject(sql, Collections.singletonMap("id", id), rowMapper);
    }
}
