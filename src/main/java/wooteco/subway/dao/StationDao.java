package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.entity.StationEntity;

@Repository
public class StationDao {

    private static final RowMapper<StationEntity> STATION_ROW_MAPPER = (resultSet, rowNum) -> new StationEntity(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );

    private final JdbcTemplate jdbcTemplate;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(StationEntity stationEntity) {
        String sql = "INSERT INTO STATION (name) VALUES (?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, stationEntity.getName());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<StationEntity> findAll() {
        String sql = "SELECT id, name FROM STATION";
        return jdbcTemplate.query(sql, STATION_ROW_MAPPER);
    }

    public StationEntity findById(Long id) {
        String sql = "SELECT id, name FROM STATION WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, STATION_ROW_MAPPER, id);
    }

    public StationEntity findByName(String name) {
        String sql = "SELECT id, name FROM STATION WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, STATION_ROW_MAPPER, name);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM STATION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
