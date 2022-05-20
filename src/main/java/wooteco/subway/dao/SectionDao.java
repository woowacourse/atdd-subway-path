package wooteco.subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;

import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Section> rowMapper = (resultSet, rowNum) -> {
        long id = resultSet.getLong("id");
        long lineId = resultSet.getLong("line_id");
        long upStationId = resultSet.getLong("up_station_id");
        long downStationId = resultSet.getLong("down_station_id");
        int distance = resultSet.getInt("distance");

        return new Section(id, lineId, upStationId, downStationId, distance);
    };

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(final long lineId, final long upStationId, final long downStationId, final int distance) {
        final String sql = "insert into section(line_id, up_station_id, down_station_id, distance) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, lineId, upStationId, downStationId, distance);
    }

    public void deleteById(long lineId, long stationId) {
        final String sql = "delete from section where line_id=? and (up_station_id=? or down_station_id=?)";
        jdbcTemplate.update(sql, lineId, stationId, stationId);
    }

    public List<Section> findAllByLineId(long lineId) {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from section where line_id=?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteByLineId(long lineId) {
        final String sql = "delete from section where line_id=?";
        jdbcTemplate.update(sql, lineId);
    }

    public List<Section> findAll() {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from section";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
