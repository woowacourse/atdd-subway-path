package wooteco.subway.dao.jdbc;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcSectionDao implements SectionDao {

    public final RowMapper<Section> SECTION_ROW_MAPPER = (rs, rowNum) -> new Section(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getInt("distance")
    );
    private final JdbcTemplate jdbcTemplate;

    public JdbcSectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Section create(Section section) {
        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("line_id", section.getLineId());
        parameters.put("up_station_id", section.getUpStationId());
        parameters.put("down_station_id", section.getDownStationId());
        parameters.put("distance", section.getDistance());

        final Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Section(
                number.longValue(),
                section.getLineId(),
                section.getUpStationId(),
                section.getDownStationId(),
                section.getDistance()
        );
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM section WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existById(Long id) {
        String sql = "SELECT exists (SELECT * FROM section WHERE id=?)";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != 0;
    }

    @Override
    public List<Section> findAllByLineId(Long lineId) {
        String sql = "SELECT * FROM section WHERE line_id=?";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER, lineId);
    }

    @Override
    public List<Section> findAll() {
        String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER);
    }

    @Override
    public void updateUpStationId(Long id, Long changeStationId, int calculateDistance) {
        String sql = "UPDATE section SET up_station_id=?, distance=? WHERE id=?";
        jdbcTemplate.update(sql, changeStationId, calculateDistance, id);
    }

    @Override
    public void updateDownStationId(Long id, Long changeStationId, int calculateDistance) {
        String sql = "UPDATE section SET down_station_id=?, distance=? WHERE id=?";
        jdbcTemplate.update(sql, changeStationId, calculateDistance, id);
    }
}
