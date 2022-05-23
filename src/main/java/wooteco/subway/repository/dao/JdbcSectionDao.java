package wooteco.subway.repository.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;
import wooteco.subway.repository.entity.SectionEntity;

@Repository
public class JdbcSectionDao implements SectionDao {

    private static final RowMapper<SectionEntity> SECTION_ROW_MAPPER = (resultSet, rowNum) -> new SectionEntity(
        resultSet.getLong("id"),
        resultSet.getLong("line_id"),
        resultSet.getLong("up_station_id"),
        resultSet.getLong("down_station_id"),
        resultSet.getInt("distance")
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcSectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Section section) {
        final String sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, section.getLineId());
            ps.setLong(2, section.getUpStationId());
            ps.setLong(3, section.getDownStationId());
            ps.setInt(4, section.getDistance());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public List<SectionEntity> findByLineId(Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER, lineId);
    }


    @Override
    public boolean update(Long sectionId, Long downStationId, int distance) {
        final String sql = "UPDATE section SET down_station_id = ?, distance = ? where id = ?";
        int updateSize = jdbcTemplate.update(sql, downStationId, distance, sectionId);
        return updateSize != 0;
    }

    @Override
    public boolean deleteById(Long sectionId) {
        final String sql = "DELETE FROM section where id = ?";
        int updateSize = jdbcTemplate.update(sql, sectionId);
        return updateSize != 0;
    }

    @Override
    public List<SectionEntity> findAll() {
        final String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER);
    }

    @Override
    public void insertAll(List<Section> sections) {
        final String sql = "INSERT INTO section(line_id, up_station_id, down_station_id, distance) VALUES(?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, sections.get(i).getLineId());
                ps.setLong(2, sections.get(i).getUpStationId());
                ps.setLong(3, sections.get(i).getDownStationId());
                ps.setLong(4, sections.get(i).getDistance());
            }

            @Override
            public int getBatchSize() {
                return sections.size();
            }
        });
    }

    @Override
    public void deleteAll() {
        final String sql = "DELETE FROM section";
        jdbcTemplate.update(sql);
    }
}
