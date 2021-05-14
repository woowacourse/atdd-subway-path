package wooteco.subway.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;

@Repository
public class StationDao {

    private SimpleJdbcInsert insertAction;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
        new Station(
            rs.getLong("id"),
            rs.getString("name")
        );

    public StationDao(DataSource dataSource) {
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        String sql = "delete from STATION where id = :id";
        namedParameterJdbcTemplate.update(sql, params);
    }

    public Station findById(Long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        String sql = "select * from STATION where id = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, params, rowMapper);
    }
}