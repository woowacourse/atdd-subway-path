package wooteco.subway.dao.section;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Repository
public class JdbcSectionDao implements SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Section> sectionRowMapper = ((rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    new Line(
                            rs.getLong("line_id"),
                            rs.getString("line_name"),
                            rs.getString("line_color"),
                            rs.getInt("line_extraFare")
                    ),
                    new Station(rs.getLong("us_id"), rs.getString("us_name")),
                    new Station(rs.getLong("ds_id"), rs.getString("ds_name")),
                    rs.getInt("distance")
            ));

    public JdbcSectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(final Section section) {
        final String sql = "INSERT INTO SECTION (line_id, up_station_id, down_station_id, distance) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, section.getLine().getId());
            ps.setLong(2, section.getUpStation().getId());
            ps.setLong(3, section.getDownStation().getId());
            ps.setInt(4, section.getDistance());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Section> findAllByLineId(final long lineId) {
        final String sql =
                "SELECT s.id AS id, s.line_id AS line_id, l.name AS line_name, l.color AS line_color, l.extraFare AS line_extraFare, "
                        + "us.id AS us_id, us.name AS us_name, ds.id AS ds_id, ds.name AS ds_name, s.distance AS distance "
                        + "FROM SECTION AS s "
                        + "JOIN STATION AS us ON us.id = s.up_station_id "
                        + "JOIN STATION AS ds ON ds.id = s.down_station_id "
                        + "JOIN LINE AS l on l.id = s.line_id "
                        + "WHERE line_id = ?";

        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    @Override
    public List<Section> findAll() {
        final String sql =
                "SELECT s.id AS id, s.line_id AS line_id, l.name AS line_name, l.color AS line_color, l.extraFare AS line_extraFare, "
                        + "us.id AS us_id, us.name AS us_name, ds.id AS ds_id, ds.name AS ds_name, s.distance AS distance "
                        + "FROM SECTION AS s "
                        + "JOIN STATION AS us on us.id = s.up_station_id "
                        + "JOIN STATION AS ds on ds.id = s.down_station_id "
                        + "JOIN LINE AS l on l.id = s.line_id";

        return jdbcTemplate.query(sql, sectionRowMapper);
    }

    @Override
    public int updateSections(final List<Section> sections) {
        final String sql = "update SECTION set up_station_id = ?, down_station_id = ?, distance = ? where id = ?";
        return jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                        Section section = sections.get(i);
                        ps.setLong(1, section.getUpStation().getId());
                        ps.setLong(2, section.getDownStation().getId());
                        ps.setInt(3, section.getDistance());
                        ps.setLong(4, section.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return sections.size();
                    }
                }).length;
    }

    @Override
    public int delete(final long sectionId) {
        final String sql = "delete from SECTION where id = ?";
        return jdbcTemplate.update(sql, sectionId);
    }

    @Override
    public int deleteByLineId(final long lineId) {
        final String sql = "delete from SECTION where line_id = ?";
        return jdbcTemplate.update(sql, lineId);
    }
}
