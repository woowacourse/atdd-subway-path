package wooteco.subway.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import wooteco.subway.domain.Station;

@Repository
public class StationDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<Station> stationRowMapper = (resultSet, rowNum) ->
            new Station(
                    resultSet.getLong("id"),
                    resultSet.getString("name"));

    public StationDao(DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Station save(Station station) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(station);
        Long id = jdbcInsert.executeAndReturnKey(param).longValue();
        return createNewObject(station, id);
    }

    private Station createNewObject(Station station, Long id) {
        Field field = ReflectionUtils.findField(Station.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, station, id);
        return station;
    }

    public List<Station> findAll() {
        String sql = "SELECT * FROM station";
        return namedParameterJdbcTemplate.query(sql, stationRowMapper);
    }

    public Optional<Station> findById(Long id) {
        String sql = "SELECT * FROM station WHERE id = :id";
        SqlParameterSource paramSource = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, paramSource, stationRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM station WHERE id = :id";
        SqlParameterSource paramSource = new MapSqlParameterSource("id", id);
        int deletedCount = namedParameterJdbcTemplate.update(sql, paramSource);
        validateRemoved(deletedCount);
        return deletedCount;
    }

    private void validateRemoved(int deletedCount) {
        if (deletedCount == 0) {
            throw new IllegalStateException("삭제하고자 하는 역이 존재하지 않습니다.");
        }
    }
}
