package wooteco.subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;

@Repository
public class SectionDao {

    private static final RowMapper<Section> SECTION_ROW_MAPPER = (rs, rowNum) -> new Section(
            rs.getLong("id"),
            new Station(rs.getLong("up_station_id"), rs.getString("up_station_name")),
            new Station(rs.getLong("down_station_id"), rs.getString("down_station_name")),
            new Distance(rs.getInt("distance"))
    );

    private final JdbcTemplate jdbcTemplate;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Sections findSectionsByLineId(Long lineId) {
        String sql =
                "SELECT SECTION.id, UP_STATION.id AS up_station_id, UP_STATION.name AS up_station_name, "
                        + "DOWN_STATION.id AS down_station_id, DOWN_STATION.name AS down_station_name, distance FROM SECTION "
                        + "INNER JOIN STATION AS UP_STATION ON SECTION.up_station_id = UP_STATION.id "
                        + "INNER JOIN STATION AS DOWN_STATION ON SECTION.down_station_id = DOWN_STATION.id "
                        + "WHERE line_id = ?";

        List<Section> sections = jdbcTemplate.query(sql, SECTION_ROW_MAPPER, lineId);
        return new Sections(sections);
    }

    public List<Section> findAll() {
        String sql =
                "SELECT SECTION.id, UP_STATION.id AS up_station_id, UP_STATION.name AS up_station_name, "
                        + "DOWN_STATION.id AS down_station_id, DOWN_STATION.name AS down_station_name, distance FROM SECTION "
                        + "INNER JOIN STATION AS UP_STATION ON SECTION.up_station_id = UP_STATION.id "
                        + "INNER JOIN STATION AS DOWN_STATION ON SECTION.down_station_id = DOWN_STATION.id";

        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER);
    }

    public Section save(Long lineId, Section section) {
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

        Long createdId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Section(createdId, section);
    }

    public void deleteAllSectionsByLineId(Long lineId) {
        String sql = "DELETE FROM SECTION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
