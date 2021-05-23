package wooteco.subway.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class SectionDao {

    private static final String SECTION_TABLE = "SECTION";
    private static final String ID = "id";
    private static final String LINE_ID = "line_id";
    private static final String UP_STATION_ID = "up_station_id";
    private static final String DOWN_STATION_ID = "down_station_id";
    private static final String DISTANCE = "distance";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        DataSource dataSource = Objects.requireNonNull(jdbcTemplate.getDataSource());
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName(SECTION_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put(LINE_ID, line.getId());
        params.put(UP_STATION_ID, section.getUpStation().getId());
        params.put(DOWN_STATION_ID, section.getDownStation().getId());
        params.put(DISTANCE, section.getDistance());

        Long sectionId = insertAction.executeAndReturnKey(params).longValue();

        return new Section(sectionId, section.getUpStation(), section.getDownStation(),
                section.getDistance());
    }

    public void deleteByLineId(Long lineId) {
        jdbcTemplate.update("delete from SECTION where line_id = ?", lineId);
    }

    public void insertSections(Line line) {
        List<Section> sections = line.getSections().getSections();
        List<Map<String, Object>> batchValues = sections.stream()
                .map(section ->
                        new HashMap<String, Object>() {{
                            put(LINE_ID, line.getId());
                            put(UP_STATION_ID, section.getUpStation().getId());
                            put(DOWN_STATION_ID, section.getDownStation().getId());
                            put(DISTANCE, section.getDistance());
                        }})
                .collect(Collectors.toList());

        insertAction.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }
}