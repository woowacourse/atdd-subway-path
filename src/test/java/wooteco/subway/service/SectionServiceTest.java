package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.dto.section.SectionResponse;

@JdbcTest
public class SectionServiceTest {
    private static final long LINE_ID = 1L;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        sectionService = new SectionService(new SectionDao(jdbcTemplate));

        createSection();
    }

    private void createSection() {
        var sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) values(?, ?, ?, ?)";
        jdbcTemplate.update(sql, LINE_ID, 1L, 2L, 2);
    }

    @Test
    @DisplayName("구간 등록시 상행역이 같은 경우 기존 구간이 변경된다.")
    void addSection2() {
        //given
        var sectionRequest = new SectionRequest(1L, 3L, 1);

        //when
        sectionService.addSection(LINE_ID, sectionRequest);

        //then
        var actual = findSection();
        var firstSection = actual.get(0);
        var secondSection = actual.get(1);

        assertAll(
                () -> assertThat(firstSection.getUpStationId()).isEqualTo(3L),
                () -> assertThat(firstSection.getDownStationId()).isEqualTo(2L),
                () -> assertThat(firstSection.getDistance()).isEqualTo(1),
                () -> assertThat(secondSection.getUpStationId()).isEqualTo(1L),
                () -> assertThat(secondSection.getDownStationId()).isEqualTo(3L),
                () -> assertThat(secondSection.getDistance()).isEqualTo(1)
        );
    }

    private List<SectionResponse> findSection() {
        var sql = "SELECT * FROM section";

        RowMapper<SectionResponse> rowMapper = (rs, rowNum) -> {
            var id = rs.getLong("id");
            var upStationId = rs.getLong("up_station_id");
            var downStationId = rs.getLong("down_station_id");
            var distance = rs.getInt("distance");
            return new SectionResponse(id, upStationId, downStationId, distance);
        };

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Test
    @DisplayName("구간 등록시 하행역이 같은 경우 기존 구간이 변경된다.")
    void addSection3() {
        //given
        var sectionRequest = new SectionRequest(3L, 2L, 1);

        //when
        sectionService.addSection(LINE_ID, sectionRequest);

        //then
        var actual = findSection();
        var firstSection = actual.get(0);
        var secondSection = actual.get(1);

        assertAll(
                () -> assertThat(firstSection.getUpStationId()).isEqualTo(1L),
                () -> assertThat(firstSection.getDownStationId()).isEqualTo(3L),
                () -> assertThat(firstSection.getDistance()).isEqualTo(1),
                () -> assertThat(secondSection.getUpStationId()).isEqualTo(3L),
                () -> assertThat(secondSection.getDownStationId()).isEqualTo(2L),
                () -> assertThat(secondSection.getDistance()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("구간 등록시 기존 구간의 하행역이 등록될 구간의 상행역인 경우")
    void addSection4() {
        //given
        var sectionRequest = new SectionRequest(2L, 3L, 1);

        //when
        sectionService.addSection(LINE_ID, sectionRequest);

        //then
        var actual = findSection();
        var firstSection = actual.get(0);
        var secondSection = actual.get(1);

        assertAll(
                () -> assertThat(firstSection.getUpStationId()).isEqualTo(1L),
                () -> assertThat(firstSection.getDownStationId()).isEqualTo(2L),
                () -> assertThat(firstSection.getDistance()).isEqualTo(2),
                () -> assertThat(secondSection.getUpStationId()).isEqualTo(2L),
                () -> assertThat(secondSection.getDownStationId()).isEqualTo(3L),
                () -> assertThat(secondSection.getDistance()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("구간 등록시 기존 구간의 상행역이 등록될 구간의 하행역인 경우")
    void addSection5() {
        //given
        var sectionRequest = new SectionRequest(3L, 1L, 1);

        //when
        sectionService.addSection(LINE_ID, sectionRequest);

        //then
        var actual = findSection();
        var firstSection = actual.get(0);
        var secondSection = actual.get(1);

        assertAll(
                () -> assertThat(firstSection.getUpStationId()).isEqualTo(1L),
                () -> assertThat(firstSection.getDownStationId()).isEqualTo(2L),
                () -> assertThat(firstSection.getDistance()).isEqualTo(2),
                () -> assertThat(secondSection.getUpStationId()).isEqualTo(3L),
                () -> assertThat(secondSection.getDownStationId()).isEqualTo(1L),
                () -> assertThat(secondSection.getDistance()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("구간 삭제시 상행 종점역이 삭제되는 경우")
    void deleteSection() {
        //given
        var sectionRequest = new SectionRequest(2L, 3L, 1);
        sectionService.addSection(LINE_ID, sectionRequest);

        //when
        sectionService.deleteSection(LINE_ID, 1L);

        //then
        var actual = findSection().get(0);
        assertAll(
                () -> assertThat(actual.getUpStationId()).isEqualTo(2L),
                () -> assertThat(actual.getDownStationId()).isEqualTo(3L),
                () -> assertThat(actual.getDistance()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("구간 삭제시 중간역이 삭제되는 경우")
    void deleteSection2() {
        //given
        var sectionRequest = new SectionRequest(2L, 3L, 1);
        sectionService.addSection(LINE_ID, sectionRequest);

        //when
        sectionService.deleteSection(LINE_ID, 2L);

        //then
        var actual = findSection().get(0);
        assertAll(
                () -> assertThat(actual.getUpStationId()).isEqualTo(1L),
                () -> assertThat(actual.getDownStationId()).isEqualTo(3L),
                () -> assertThat(actual.getDistance()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("구간 삭제시 하행 종점역이 삭제되는 경우")
    void deleteSection3() {
        //given
        var sectionRequest = new SectionRequest(2L, 3L, 1);
        sectionService.addSection(LINE_ID, sectionRequest);

        //when
        sectionService.deleteSection(LINE_ID, 3L);

        //then
        var actual = findSection().get(0);
        assertAll(
                () -> assertThat(actual.getUpStationId()).isEqualTo(1L),
                () -> assertThat(actual.getDownStationId()).isEqualTo(2L),
                () -> assertThat(actual.getDistance()).isEqualTo(2)
        );
    }
}
