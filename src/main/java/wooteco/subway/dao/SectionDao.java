package wooteco.subway.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import java.util.*;

@Repository
public class SectionDao {

    private static final String FIND_ALL_SECTIONS_SQL = "SELECT SC.id, SC.up_station_id, SC.down_station_id, SC.distance, SC.line_id, " +
            "S1.name as up_name, S2.name as down_name, L.id as line_id, L.name as line_name, L.color, L.extra_fare " +
            "FROM SECTION SC " +
            "JOIN STATION S1 ON SC.up_station_id=S1.id " +
            "JOIN STATION S2 ON SC.down_station_id=S2.id " +
            "JOIN LINE L ON SC.line_id=L.id ";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<Section> rowMapper = (rs, row) -> {
        final long id = rs.getLong("id");
        final long lineId = rs.getLong("line_id");
        final long upStationId = rs.getLong("up_station_id");
        final long downStationId = rs.getLong("down_station_id");
        final int distance = rs.getInt("distance");

        final String lineName = rs.getString("line_name");
        final String lineColor = rs.getString("color");
        final int extraFare = rs.getInt("extra_fare");

        return new Section(
                id,
                new Station(upStationId, rs.getString("up_name")),
                new Station(downStationId, rs.getString("down_name")),
                distance,
                new Line(lineId, lineName, lineColor, extraFare)
        );
    };

    public SectionDao(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Section save(final Section section) {
        final String sql = "INSERT INTO SECTION(up_station_id, down_station_id, distance, line_id) " +
                "VALUES (:upStationId, :downStationId, :distance, :lineId)";

        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", section.getLine().getId());
        params.put("upStationId", section.getUpStation().getId());
        params.put("downStationId", section.getDownStation().getId());
        params.put("distance", section.getDistance());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
        final long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Section(id, section.getUpStation(), section.getDownStation(), section.getDistance(), section.getLine());
    }

    public void saveAll(final List<Section> sections) {
        final String sql = "INSERT INTO SECTION(up_station_id, down_station_id, distance, line_id) " +
                "VALUES (:upStationId, :downStationId, :distance, :lineId)";

        List<MapSqlParameterSource> params = new ArrayList<>();
        for (Section section : sections) {
            MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("upStationId", section.getUpStation().getId());
            source.addValue("downStationId", section.getDownStation().getId());
            source.addValue("distance", section.getDistance());
            source.addValue("lineId", section.getLine().getId());
            params.add(source);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, params.toArray(MapSqlParameterSource[]::new));
    }

    public List<Section> findAll() {
        final String sql = FIND_ALL_SECTIONS_SQL;

        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public List<Section> findAllByLineId(final long lineId) {
        final String sql = FIND_ALL_SECTIONS_SQL + "WHERE SC.line_id=:lineId";

        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", lineId);

        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(params), rowMapper);
    }

    public void deleteByLineId(final long lineId) {
        final String sql = "DELETE FROM SECTION WHERE line_id = :lineId";
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", lineId);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
