package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import wooteco.subway.domain.Section;
import wooteco.subway.dto.section.SectionRequest;

@JdbcTest
public class SectionDaoTest {

    private static final int DEFAULT_DISTANCE = 1;
    private static final Long LINE_ID = 1L;
    private static final long UP_STATION_ID = 1L;
    private static final long DOWN_STATION_ID = 2L;
    private SectionDao sectionDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.sectionDao = new SectionDao(jdbcTemplate);

        var sectionRequest = new SectionRequest(UP_STATION_ID, DOWN_STATION_ID, DEFAULT_DISTANCE);
        insertSection(LINE_ID, sectionRequest);
    }

    private void insertSection(Long lineId, SectionRequest sectionRequest) {
        sectionDao.save(lineId, sectionRequest);
    }

    @Test
    void createSection() {
        var section = findSection();

        assertAll(
                () -> assertThat(section.getUpStationId()).isEqualTo(UP_STATION_ID),
                () -> assertThat(section.getDownStationId()).isEqualTo(DOWN_STATION_ID),
                () -> assertThat(section.getDistance()).isEqualTo(DEFAULT_DISTANCE)
        );
    }

    private Section findSection() {
        var sql = "SELECT * FROM section";

        RowMapper<Section> rowMapper = (rs, rowNum) -> {
            var id = rs.getLong("id");
            var upStationId = rs.getLong("up_station_id");
            var downStationId = rs.getLong("down_station_id");
            var distance = rs.getInt("distance");
            return new Section(id, upStationId, downStationId, distance);
        };

        return jdbcTemplate.query(sql, rowMapper).get(0);
    }

    @Test
    void deleteSection() {
        var section = findSection();

        assertDoesNotThrow(() -> sectionDao.delete(section));
    }

    @Test
    void deleteByInvalidSectionId() {
        var invalidSectionId = -1L;
        var invalidSection = new Section(invalidSectionId, UP_STATION_ID, DOWN_STATION_ID, DEFAULT_DISTANCE);

        assertThatThrownBy(() -> sectionDao.delete(invalidSection))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 정보와 일치하는 구간이 없습니다.");
    }

    @Test
    void findByLineId() {
        var section = sectionDao.findByLineId(LINE_ID).get(0);

        assertAll(
                () -> assertThat(section.getUpStationId()).isEqualTo(UP_STATION_ID),
                () -> assertThat(section.getDownStationId()).isEqualTo(DOWN_STATION_ID),
                () -> assertThat(section.getDistance()).isEqualTo(DEFAULT_DISTANCE)
        );
    }

    @Test
    void updateSection() {
        //given
        var newDistance = 100;
        var sectionId = findSection().getId();
        var section = new Section(sectionId, UP_STATION_ID, DOWN_STATION_ID, newDistance);

        //when
        sectionDao.update(section);

        //then
        var result = findSection();
        assertAll(
                () -> assertThat(result.getDistance()).isEqualTo(newDistance),
                () -> assertThat(result.getUpStationId()).isEqualTo(UP_STATION_ID),
                () -> assertThat(result.getDownStationId()).isEqualTo(DOWN_STATION_ID)
        );
    }

    @Test
    @DisplayName("존재하지 않는 구간 아이디로 생성시 예외발생")
    void updateInvalidSection() {
        var invalidSectionId = -1L;
        var invalidSection = new Section(invalidSectionId, UP_STATION_ID, DOWN_STATION_ID, DEFAULT_DISTANCE);

        assertThatThrownBy(() -> sectionDao.update(invalidSection))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 정보와 일치하는 구간이 없습니다.");
    }

    @Test
    void findAll() {
        List<Section> sections = sectionDao.findAll();
        var sectionId = findSection().getId();
        var expectedSection = new Section(sectionId, UP_STATION_ID, DOWN_STATION_ID, DEFAULT_DISTANCE);

        assertAll(
                () -> assertThat(sections.size()).isEqualTo(1),
                () -> assertThat(sections.get(0)).isEqualTo(expectedSection)
        );
    }

    @Test
    void findByStationId() {
        //given
        var newLineId = 100L;
        var newDownStationId = 3L;
        insertSection(newLineId, new SectionRequest(DOWN_STATION_ID, newDownStationId, DEFAULT_DISTANCE));

        //when
        var results = sectionDao.findByStationId(DOWN_STATION_ID);

        //then
        assertThat(results.size()).isEqualTo(2);
    }
}
