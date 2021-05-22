package wooteco.subway.line.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LineDao {

    private static final String LINE_TABLE = "LINE";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String COLOR = "color";

    private static final String SECTION_ID = "SECTION_ID";
    private static final String LINE_ID = "line_id";
    private static final String LINE_NAME = "LINE_NAME";
    private static final String LINE_COLOR = "LINE_COLOR";
    private static final String UP_STATION_ID = "UP_STATION_ID";
    private static final String UP_STATION_NAME = "UP_STATION_Name";
    private static final String DOWN_STATION_ID = "DOWN_STATION_ID";
    private static final String DOWN_STATION_Name = "DOWN_STATION_Name";
    private static final String SECTION_DISTANCE = "SECTION_DISTANCE";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        DataSource dataSource = Objects.requireNonNull(jdbcTemplate.getDataSource());
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName(LINE_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put(ID, line.getId());
        params.put(NAME, line.getName());
        params.put(COLOR, line.getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor());
    }

    public Line findById(Long id) {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id " +
                "WHERE L.id = ?";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, id);
        return mapLine(result);
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        Object[] params = {newLine.getName(), newLine.getColor(), newLine.getId()};
        jdbcTemplate.update(sql, params);
    }

    public List<Line> findAll() {
        String sql = "select L.id as line_id, L.name as line_name, L.color as line_color, " +
                "S.id as section_id, S.distance as section_distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from LINE L \n" +
                "left outer join SECTION S on L.id = S.line_id " +
                "left outer join STATION UST on S.up_station_id = UST.id " +
                "left outer join STATION DST on S.down_station_id = DST.id ";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<Long, List<Map<String, Object>>> resultByLine = result.stream()
                .collect(Collectors.groupingBy(it -> (Long) it.get("line_id")));
        return resultByLine.values().stream()
                .map(this::mapLine)
                .collect(Collectors.toList());
    }

    private Line mapLine(List<Map<String, Object>> result) {
        if (result.size() == 0) {
            throw new RuntimeException();
        }

        List<Section> sections = extractSections(result);

        return new Line(
                (Long) result.get(0).get(LINE_ID),
                (String) result.get(0).get(LINE_NAME),
                (String) result.get(0).get(LINE_COLOR),
                new Sections(sections));
    }

    private List<Section> extractSections(List<Map<String, Object>> result) {
        if (result.isEmpty() || result.get(0).get(SECTION_ID) == null) {
            return Collections.emptyList();
        }
        return result.stream()
                .collect(Collectors.groupingBy(it -> it.get(SECTION_ID)))
                .entrySet()
                .stream()
                .map(this::convertListEntryToSection)
                .collect(Collectors.toList());
    }

    private Section convertListEntryToSection(Map.Entry<Object, List<Map<String, Object>>> listEntry) {
        Map<String, Object> map = listEntry.getValue().get(0);

        Long id = (Long) listEntry.getKey();
        Station upStation = new Station(
                (Long) map.get(UP_STATION_ID),
                (String) map.get(UP_STATION_NAME));
        Station downStation = new Station(
                (Long) map.get(DOWN_STATION_ID),
                (String) map.get(DOWN_STATION_Name));
        int distance = (int) map.get(SECTION_DISTANCE);

        return new Section(id, upStation, downStation, distance);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}