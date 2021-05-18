package wooteco.subway.station.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.station.domain.Station;

@Repository
public class StationDao {

    private static final String STATION_TABLE = "STATION";
    private static final String ID = "id";

    private static final RowMapper<Station> STATION_ROW_MAPPER = (rs, rowNum) ->
            new Station(
                    rs.getLong(ID),
                    rs.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        DataSource dataSource = Objects.requireNonNull(jdbcTemplate.getDataSource());
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName(STATION_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, STATION_ROW_MAPPER);
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Station> findById(Long id) {
        String sql = "select * from STATION where id = ?";
        try {
            Station station = jdbcTemplate.queryForObject(sql, STATION_ROW_MAPPER, id);
            return Optional.ofNullable(station);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }

    }
}