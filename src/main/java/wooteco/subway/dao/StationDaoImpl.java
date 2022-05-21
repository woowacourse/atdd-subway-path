package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Repository
public class StationDaoImpl implements StationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public StationDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Station save(Station station) {
        final String sql = "INSERT INTO station (name) VALUES (:name)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final SqlParameterSource params = new MapSqlParameterSource("name", station.getName());
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Station(id, station.getName());
    }

    @Override
    public List<Station> findAll() {
        final String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, new StationMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        final String sql = "DELETE FROM station where id = :id";
        final SqlParameterSource params = new MapSqlParameterSource("id", id);
        int updateSize = jdbcTemplate.update(sql, params);
        return updateSize != 0;
    }

    @Override
    public boolean existsByName(String name) {
        final String sql = "SELECT EXISTS (SELECT * FROM station WHERE name = :name)";
        final SqlParameterSource params = new MapSqlParameterSource("name", name);
        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    @Override
    public Station findById(Long id) {
        final String sql = "SELECT * FROM station where id = :id";
        final SqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, params, new StationMapper());
    }

    @Override
    public List<Station> findById(List<Long> ids) {
        final String sql = "SELECT * FROM station where id in (:ids)";
        final SqlParameterSource params = new MapSqlParameterSource("ids", ids);
        return jdbcTemplate.query(sql, params, new StationMapper());
    }

    private static class StationMapper implements RowMapper<Station> {
        public Station mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new Station(rs.getLong("id"),
                    rs.getString("name"));
        }
    }
}
