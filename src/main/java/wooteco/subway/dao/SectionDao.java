package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.section.Section;
import wooteco.subway.entity.SectionEntity;

@Repository
public class SectionDao {

    private static final RowMapper<SectionEntity> SECTION_ROW_MAPPER = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getInt("distance")
    );

    private final JdbcTemplate jdbcTemplate;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Long lineId, Section section) {
        String sql = "INSERT INTO SECTION (line_id, up_station_id, down_station_id, distance) VALUES(?, ?, ?, ?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, lineId);
            ps.setLong(2, section.getUpStation().getId());
            ps.setLong(3, section.getDownStation().getId());
            ps.setInt(4, section.getDistance().getValue());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM SECTION";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER);
    }

    public List<SectionEntity> findSectionsByLineId(Long lineId) {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER, lineId);
    }

    // TODO: 제거
    public void deleteAllSectionsByLineId(Long lineId) {
        String sql = "DELETE FROM SECTION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
