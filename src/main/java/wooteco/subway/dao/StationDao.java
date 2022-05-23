package wooteco.subway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import wooteco.subway.domain.Station;

@Component
public class StationDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StationDao(DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Station save(Station station) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(station);
        Long id = jdbcInsert.executeAndReturnKey(param).longValue();
        return new Station(id, station.getName());
    }

    private Station mapToStation(ResultSet resultSet) throws SQLException {
        return new Station(
                resultSet.getLong("id"),
                resultSet.getString("name"));
    }

    public List<Station> findAll() {
        String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> mapToStation(resultSet));
    }

    public Optional<Station> findById(Long id) {
        String sql = "SELECT * FROM station WHERE id = :id";
        SqlParameterSource paramSource = new MapSqlParameterSource("id", id);
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, paramSource, (resultSet, rowNum) -> mapToStation(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM station WHERE id = :id";
        SqlParameterSource paramSource = new MapSqlParameterSource("id", id);
        int deletedCount = jdbcTemplate.update(sql, paramSource);
        validateRemoved(deletedCount);
        return deletedCount;
    }

    private void validateRemoved(int deletedCount) {
        if (deletedCount == 0) {
            throw new IllegalStateException("삭제하고자 하는 역이 존재하지 않습니다.");
        }
    }

    public void deleteAll() {
        String sql = "TRUNCATE TABLE station";
        jdbcTemplate.update(sql, new MapSqlParameterSource());
        String resetIdSql = "ALTER TABLE station ALTER COLUMN id RESTART WITH 1";
        jdbcTemplate.update(resetIdSql, new MapSqlParameterSource());
    }
}
