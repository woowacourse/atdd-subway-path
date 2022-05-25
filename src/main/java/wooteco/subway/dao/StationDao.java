package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Station;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Station> STATION_ROW_MAPPER = ((rs, rowNum) ->
            new Station(rs.getLong("id"),
                    rs.getString("name"))
    );

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Station save(Station station) {
        final String sql = "insert into Station (name) values (?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, station.getName());
            return statement;
        }, keyHolder);

        return new Station(
                Objects.requireNonNull(keyHolder.getKey()).longValue(),
                station.getName()
        );
    }

    public int isExistStation(Station station) {
        String sql = "select EXISTS (select name from station where name = ?) as success";
        return jdbcTemplate.queryForObject(sql, Integer.class, station.getName());
    }

    public List<Station> findAll() {
        final String sql = "select id, name from Station";
        return jdbcTemplate.query(sql, STATION_ROW_MAPPER);
    }

    public int deleteById(Long id) {
        final String sql = "delete from Station where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Station> getByIds(List<Long> sortedStationIds) {
        String sortedStationValues = sortedStationIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        String sql = String.format("SELECT id, name FROM STATION WHERE id IN (%s)", sortedStationValues);

        List<Station> stations = jdbcTemplate.query(sql, STATION_ROW_MAPPER);
        return sort(sortedStationIds, stations);
    }

    private List<Station> sort(List<Long> sortedIds, List<Station> stations) {
        return sortedIds.stream()
                .flatMap(sortedId -> stations.stream()
                        .filter(station -> station.getId().equals(sortedId)))
                .collect(Collectors.toList());
    }
}
