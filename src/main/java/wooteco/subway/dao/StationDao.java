package wooteco.subway.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.station.Station;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StationDao {
    private static final RowMapper<Station> ACTOR_ROW_MAPPER = (resultSet, rowNum) ->
            new Station(resultSet.getLong("id"), resultSet.getString("name"));

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public StationDao(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(station);
        Long id = insertActor.executeAndReturnKey(parameterSource).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return namedParameterJdbcTemplate.query(sql, ACTOR_ROW_MAPPER);
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public Station findById(Long id) {
        String sql = "select * from STATION where id = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, ACTOR_ROW_MAPPER);
    }

    public List<Station> findByIdIn(List<Long> ids) {
        String sql = "select * from STATION where id in (:ids)";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.query(sql, sqlParameterSource, ACTOR_ROW_MAPPER);
    }

    public boolean existByName(String name) {
        String sql = "select EXISTS (select id from STATION where name = :name) as success";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("name", name);
        return Boolean.TRUE.equals(namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, Boolean.class));
    }
}
