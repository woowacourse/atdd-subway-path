package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.datanotfound.StationNotFoundException;

@Repository
public class StationJdbcDao implements StationDao {

    private final JdbcTemplate jdbcTemplate;

    public StationJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Station> stationRowMapper() {
        return (rs, rowNum) ->
                new Station(rs.getLong("id"), rs.getString("name"));
    }

    @Override
    public Station save(Station station) {
        final String sql = "INSERT INTO station (name, deleted) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, station.getName());
            ps.setBoolean(2, false);
            return ps;
        }, keyHolder);
        return new Station(Objects.requireNonNull(keyHolder.getKey()).longValue(), station.getName());
    }

    @Override
    public boolean existByName(String name) {
        final String sql = "SELECT EXISTS (SELECT * FROM station WHERE name = (?) AND deleted = (?))";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, name, false));
    }

    @Override
    public List<Station> findAll() {
        final String sql = "SELECT * FROM station WHERE deleted = (?)";
        return jdbcTemplate.query(sql, stationRowMapper(), false);
    }

    @Override
    public Station findById(Long id) {
        try {
            final String sql = "SELECT * FROM station WHERE id = (?) AND deleted = (?)";
            return jdbcTemplate.queryForObject(sql, stationRowMapper(), id, false);
        } catch (EmptyResultDataAccessException e) {
            throw new StationNotFoundException("존재하지 않는 역입니다.");
        }
    }

    @Override
    public int deleteById(Long id) {
        final String sql = "UPDATE station SET deleted = (?) WHERE id = (?)";
        return jdbcTemplate.update(sql, true, id);
    }

    @Override
    public void deleteByExistName(String name) {
        final String sql = "DELETE FROM station WHERE EXISTS(SELECT * FROM station WHERE name = (?))";
        jdbcTemplate.update(sql, name);
    }
}
