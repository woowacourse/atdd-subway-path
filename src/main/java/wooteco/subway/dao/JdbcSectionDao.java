package wooteco.subway.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Section;

@Repository
public class JdbcSectionDao implements SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;
    private final RowMapper<Section> sectionRowMapper = (rs, rowNum) -> new Section(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getInt("distance"),
            rs.getLong("line_order")
    );

    public JdbcSectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Section section) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(section);
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public void updateLineOrderByInc(final long lineId, final Long lineOrder) {
        final String sql = "UPDATE \"SECTION\" "
                + "SET line_order = line_order + 1 "
                + "WHERE line_id = (?) AND line_order >= (?)";
        jdbcTemplate.update(sql, lineId, lineOrder);
    }

    @Override
    public boolean existByLineId(final long lineId) {
        final String sql = "SELECT EXISTS ("
                + "SELECT * FROM \"SECTION\" WHERE line_id = (?)"
                + ")";
        return jdbcTemplate.queryForObject(sql, boolean.class, lineId);
    }

    @Override
    public List<Section> findAllByLineId(final long lineId) {
        final String sql = "SELECT * from \"SECTION\" WHERE line_id = (?)";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "DELETE FROM \"SECTION\" WHERE id = (?)";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Section> findByLineIdAndStationId(final long lineId, final long stationId) {
        final String sql = "SELECT * FROM \"SECTION\" "
                + "WHERE line_id = (?) "
                + "AND (up_station_id = (?) OR down_station_id = (?))";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId, stationId, stationId);
    }

    @Override
    public void updateLineOrderByDec(long lineId, Long lineOrder) {
        final String sql = "UPDATE \"SECTION\" "
                + "SET line_order = line_order - 1 "
                + "WHERE line_id = (?) AND line_order > (?)";
        jdbcTemplate.update(sql, lineId, lineOrder);
    }

    @Override
    public List<Section> findAll() {
        final String sql = "SELECT * FROM \"SECTION\"";
        return jdbcTemplate.query(sql, sectionRowMapper);
    }
}
