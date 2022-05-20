package wooteco.subway.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import wooteco.subway.dao.dto.SectionDto;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

@Component
public class SectionDao {
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SectionDao(DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void save(Section section, Long lineId) {
        save(section, lineId, 0);
    }

    private void save(Section section, Long lineId, int index) {
        SectionDto sectionDto = new SectionDto(section, lineId, index);
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(sectionDto);
        jdbcInsert.execute(paramSource);
    }

    public void saveAll(Sections sections, Long lineId) {
        LinkedList<Section> values = sections.getSections();
        for (int i = 0; i < values.size(); i++) {
            Section section = values.get(i);
            update(section, lineId, i);
        }
    }

    private void update(Section section, Long lineId, int index) {
        if (Objects.isNull(section.getId())) {
            save(section, lineId, index);
            return;
        }
        String sql = "UPDATE section "
                + "SET up_station_id = :upStationId, down_station_id = :downStationId, distance = :distance, index_num = :indexNum "
                + "WHERE id = :id";

        // SectionDto sectionDto = new SectionDto(section, lineId, index);
        // SqlParameterSource paramSource = new BeanPropertySqlParameterSource(sectionDto);
        SqlParameterSource paramSource = new MapSqlParameterSource("upStationId", section.getUpStationId())
                .addValue("downStationId", section.getDownStationId())
                .addValue("distance", section.getDistance())
                .addValue("indexNum", index)
                .addValue("id", section.getId());
        jdbcTemplate.update(sql, paramSource);
    }

    public List<Section> findAll() {
        String sql = "SELECT "
                + "sec.id, sec.distance, "
                + "sec.up_station_id, us.name up_station_name,"
                + "sec.down_station_id, ds.name down_station_name "
                + "FROM section AS sec "
                + "JOIN station AS us ON sec.up_station_id = us.id "
                + "JOIN station AS ds ON sec.down_station_id = ds.id ";

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> mapToSection(resultSet));
    }

    private Section mapToSection(ResultSet resultSet) throws SQLException {
        Long upStationId = resultSet.getLong("up_station_id");
        String upStationName = resultSet.getString("up_station_name");

        Long downStationId = resultSet.getLong("down_station_id");
        String downStationName = resultSet.getString("down_station_name");

        return new Section(
                resultSet.getLong("id"),
                new Station(upStationId, upStationName),
                new Station(downStationId, downStationName),
                resultSet.getInt("distance")
        );
    }

    public int deleteById(Section section) {
        String sql = "DELETE FROM section WHERE id = :id";
        SqlParameterSource paramSource = new MapSqlParameterSource("id", section.getId());
        int deletedCount = jdbcTemplate.update(sql, paramSource);
        validateRemoved(deletedCount);
        return deletedCount;
    }

    public int deleteByLine(Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = :lineId";
        SqlParameterSource paramSource = new MapSqlParameterSource("lineId", lineId);
        int deletedCount = jdbcTemplate.update(sql, paramSource);
        validateRemoved(deletedCount);
        return deletedCount;
    }

    private void validateRemoved(int count) {
        if (count == 0) {
            throw new IllegalStateException("삭제할 구간이 존재하지 않습니다.");
        }
    }
}
