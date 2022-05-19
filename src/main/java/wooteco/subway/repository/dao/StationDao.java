package wooteco.subway.repository.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import wooteco.subway.repository.dao.entity.StationEntity;

@Component
public class StationDao {

    private static final RowMapper<StationEntity> STATION_ENTITY_ROW_MAPPER =
            (resultSet, rowNum) -> new StationEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public StationDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("Station")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(StationEntity stationEntity) {
        final BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(stationEntity);
        final Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);

        return generatedId.longValue();
    }

    public List<StationEntity> findAll() {
        final String sql = "SELECT id, name FROM Station";

        return jdbcTemplate.query(sql, STATION_ENTITY_ROW_MAPPER);
    }

    public Optional<StationEntity> findById(Long id) {
        final String sql = "SELECT id, name FROM Station WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, STATION_ENTITY_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM Station WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    public boolean existsByName(String name) {
        final String sql = "SELECT EXISTS (SELECT name FROM station WHERE name = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, name));
    }

    public Boolean existsById(Long id) {
        final String sql = "SELECT EXISTS (SELECT id FROM station WHERE id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
