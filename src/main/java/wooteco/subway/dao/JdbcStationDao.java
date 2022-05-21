package wooteco.subway.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;

@Repository
public class JdbcStationDao implements StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;
    private final RowMapper<Station> stationRowMapper = (resultSet, rowNum) -> new Station(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );

    public JdbcStationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("STATION")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Station station) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(station);
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Station> findAll() {
        final String sql = "SELECT * FROM STATION";
        return jdbcTemplate.query(sql, stationRowMapper);
    }

    @Override
    public void deleteById(final Long stationId) {
        final String sql = "DELETE FROM STATION WHERE id = (?)";
        jdbcTemplate.update(sql, stationId);
    }

    @Override
    public Station findById(final Long stationId) {
        final String sql = "SELECT * FROM STATION WHERE id = (?)";
        return jdbcTemplate.queryForObject(sql, stationRowMapper, stationId);
    }

    @Override
    public boolean existByName(final Station station) {
        final String sql = "SELECT EXISTS (SELECT * FROM STATION WHERE name = (?))";
        return jdbcTemplate.queryForObject(sql, Boolean.class, station.getName());
    }

    @Override
    public boolean existById(final Long stationId) {
        final String sql = "SELECT EXISTS (SELECT * FROM STATION WHERE id = (?))";
        return jdbcTemplate.queryForObject(sql, Boolean.class, stationId);
    }
}
