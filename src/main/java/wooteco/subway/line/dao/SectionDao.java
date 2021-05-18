package wooteco.subway.line.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SectionDao {
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    public Section insert(Line line, Section section) {
        Map<String, Object> params = new HashMap();
        params.put("line_id", line.getId());
        params.put("up_station_id", section.getUpStation()
                                           .getId());
        params.put("down_station_id", section.getDownStation()
                                             .getId());
        params.put("distance", section.getDistance());
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params)
                                         .longValue();
        return new Section(sectionId, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public void deleteByLineId(Long lineId) {
        jdbcTemplate.update("delete from SECTION where line_id = ?", lineId);
    }

    public void insertSections(Line line) {
        List<Section> sections = line.getSections()
                                     .getSections();
        List<Map<String, Object>> batchValues = sections.stream()
                                                        .map(section -> {
                                                            Map<String, Object> params = new HashMap<>();
                                                            params.put("line_id", line.getId());
                                                            params.put("up_station_id", section.getUpStation()
                                                                                               .getId());
                                                            params.put("down_station_id", section.getDownStation()
                                                                                                 .getId());
                                                            params.put("distance", section.getDistance());
                                                            return params;
                                                        })
                                                        .collect(Collectors.toList());

        simpleJdbcInsert.executeBatch(batchValues.toArray(new Map[sections.size()]));
    }

    public List<Section> findAll() {
        String sql = "select S.id as section_id, S.distance as distance, " +
                "UST.id as up_station_id, UST.name as up_station_name, " +
                "DST.id as down_station_id, DST.name as down_station_name " +
                "from SECTION S " +
                "left outer join STATION UST on UST.ID = S.UP_STATION_ID " +
                "left outer join STATION DST on DST.ID = S.DOWN_STATION_ID";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        Map<Long, List<Map<String, Object>>> resultBySection = result.stream()
                                                                  .collect(Collectors.groupingBy(it -> (Long) it.get("section_id")));
        return resultBySection.values().stream()
                              .map(this::mapSection)
                              .collect(Collectors.toList());
    }

    private Section mapSection(List<Map<String, Object>> result) {
        if (result.size() == 0) {
            throw new RuntimeException();
        }

        Station upStation = new Station((Long)result.get(0).get("up_station_id"), (String) result.get(0).get("up_station_name"));
        Station downStation = new Station((Long)result.get(0).get("down_station_id"), (String) result.get(0).get("down_station_name"));

        return new Section(
                (Long) result.get(0).get("section_id"),
                upStation,
                downStation,
                (Integer) result.get(0).get("distance")
        );
    }
}