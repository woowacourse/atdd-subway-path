package wooteco.subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;

@Repository
public class StationJdbcDao implements StationDao{

    private final JdbcTemplate jdbcTemplate;

    public StationJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Station> stationRowMapper = (resultSet, rowNum) ->
            new Station(resultSet.getLong("id"),
                    resultSet.getString("name"));

    @Override
    public Station save(Station station) {
        final String sql = "insert into station (name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, station.getName());
            return ps;
        }, keyHolder);
        return new Station(keyHolder.getKey().longValue(), station.getName());
    }

    @Override
    public Station findById(long id) {
        final String sql = "select * from station where id = (?)";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Station(rs.getLong("id"), rs.getString("name")), id);
    }

    public List<Station> findByIds(List<Long> ids) {
        String sql = String.join(",", Collections.nCopies(ids.size(), "?"));

        return jdbcTemplate.query(
                String.format("select * from station where id in (%s)", sql),
                ids.toArray(), stationRowMapper);
    }

    @Override
    public boolean isExistById(Long id) {
        final String sql = "select exists(select * from station where id = (?)) ";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public boolean isExistByName(String name) {
        final String sql = "select exists(select * from station where name = (?)) ";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    @Override
    public List<Station> findAll() {
        final String sql = "select * from station";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Station(rs.getLong("id"), rs.getString("name")));
    }

    @Override
    public int delete(long id) {
        final String sql = "delete from station where id = (?)";
        return jdbcTemplate.update(sql, id);
    }
}
