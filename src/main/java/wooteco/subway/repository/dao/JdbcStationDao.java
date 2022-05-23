package wooteco.subway.repository.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.entity.StationEntity;

@Repository
public class JdbcStationDao implements StationDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<StationEntity> STATION_ROW_MAPPER = (resultSet, rowNum) -> new StationEntity(
        resultSet.getLong("id"),
        resultSet.getString("name")
    );

    public JdbcStationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Station Station) {
        final String sql = "INSERT INTO station (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, Station.getName());
            return ps;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();

        return id;
    }

    @Override
    public List<StationEntity> findAll() {
        final String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, STATION_ROW_MAPPER);
    }


    @Override
    public boolean deleteById(Long id) {
        final String sql = "DELETE FROM station where id = ?";
        int updateSize = jdbcTemplate.update(sql, id);
        return updateSize != 0;
    }

    @Override
    public boolean existsByName(String name) {
        final String sql = "SELECT EXISTS (SELECT * FROM station WHERE name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    @Override
    public StationEntity findById(Long id) {
        final String sql = "SELECT * FROM station where id = ?";
        return jdbcTemplate.queryForObject(sql, STATION_ROW_MAPPER, id);
    }

    @Override
    public List<StationEntity> findById(List<Long> ids) {
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        final String sql = "SELECT * FROM station where id in (:ids)";
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
            jdbcTemplate);
        return namedParameterJdbcTemplate.query(sql, parameters, STATION_ROW_MAPPER);
    }
}
