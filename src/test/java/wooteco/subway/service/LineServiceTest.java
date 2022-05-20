package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.dto.section.SectionResponse;

@JdbcTest
class LineServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineService lineService;
    private Long upStationId;
    private Long downStationId;
    private LineRequest lineRequest;

    @BeforeEach
    void setUp() {
        lineService = new LineService(
                new LineDao(jdbcTemplate),
                new SectionDao(jdbcTemplate),
                new StationDao(jdbcTemplate)
        );
        upStationId = insertStation("테스트1역");
        downStationId = insertStation("테스트2역");
        lineRequest = new LineRequest("테스트호선", "테스트색", upStationId, downStationId, 2);
    }

    private Long insertStation(String name) {
        var keyHolder = new GeneratedKeyHolder();
        var sql = "INSERT INTO station (name) values(?)";

        jdbcTemplate.update(con -> {
            var statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, name);
            return statement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Test
    @DisplayName("노선 생성")
    void saveLine() {
        var lineResponse = lineService.createLine(lineRequest);
        var upStationResponse = lineResponse.getStations().get(0);
        var downStationResponse = lineResponse.getStations().get(1);

        assertAll(
                () -> assertThat(upStationResponse.getId()).isEqualTo(upStationId),
                () -> assertThat(downStationResponse.getId()).isEqualTo(downStationId),
                () -> assertThat(lineResponse.getName()).isEqualTo("테스트호선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("테스트색")
        );
    }

    @Test
    @DisplayName("중복 노선 생성시 예외 발생")
    void duplicateLineName() {
        lineService.createLine(lineRequest);

        assertThatThrownBy(() -> lineService.createLine(lineRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선 조회")
    void findLine() {
        //given
        var lineResponse = lineService.createLine(lineRequest);

        //when
        var findLineResponse = lineService.findLineInfos(lineResponse.getId());

        //then
        assertAll(
                () -> assertThat(findLineResponse.getId()).isEqualTo(lineResponse.getId()),
                () -> assertThat(findLineResponse.getName()).isEqualTo("테스트호선"),
                () -> assertThat(findLineResponse.getColor()).isEqualTo("테스트색")
        );
    }

    @Test
    @DisplayName("노선 조회 실패")
    void findLineFail() {
        assertThatThrownBy(() -> lineService.findLineInfos(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("노선 목록 조회")
    void findAllLine() {
        //given
        var createResponse = lineService.createLine(lineRequest);

        //when
        var lines = lineService.findAll();
        var findResponse = lines.get(0);

        //then
        assertThat(findResponse).isEqualTo(createResponse);
    }

    @Test
    @DisplayName("노선 업데이트 성공")
    void updateLine() {
        //given
        var lineResponse = lineService.createLine(lineRequest);
        var id = lineResponse.getId();

        //when
        lineService.updateById(id, "2호선", "green");
        var lineInfos = lineService.findLineInfos(id);

        //then
        assertAll(
                () -> assertThat(lineInfos.getName()).isEqualTo("2호선"),
                () -> assertThat(lineInfos.getColor()).isEqualTo("green")
        );
    }

    @Test
    @DisplayName("노선 업데이트 실패")
    void failUpdateLine() {
        lineService.createLine(lineRequest);

        var upStationId2 = insertStation("테스트3역");
        var downStationId2 = insertStation("테스트4역");
        var lineRequest2 = new LineRequest("테스트2호선", "테스트2색", upStationId2, downStationId2, 1);
        var lineResponse2 = lineService.createLine(lineRequest2);

        assertAll(
                () -> assertThatThrownBy(() -> lineService.updateById(-1L, "3호선", "orange"))
                        .isInstanceOf(NoSuchElementException.class),
                () -> assertThatThrownBy(() -> lineService.updateById(lineResponse2.getId(), "테스트호선", "테스트2색"))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> lineService.updateById(lineResponse2.getId(), "테스트2호선", "테스트색"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("노선 삭제")
    void deleteLine() {
        //given
        var lineResponse = lineService.createLine(lineRequest);
        var id = lineResponse.getId();

        //when
        lineService.deleteById(id);
        var actual = lineService.findAll().stream()
                .noneMatch(it -> it.getId().equals(id));

        //then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("없는 노선 삭제요청 시 예외 발생")
    void invalidLine() {
        assertThatThrownBy(() -> lineService.deleteById(-1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("구간 생성시 상행역이 같을 경우 기존 구간을 변경한다.")
    void createSection1() {
        //given
        var lineResponse = lineService.createLine(lineRequest);
        var id = lineResponse.getId();
        var testStationId = insertStation("테스트3역");

        //when
        lineService.addSection(id, new SectionRequest(upStationId, testStationId, 1));

        //then
        assertThat(findSectionByLineId(id)).contains(
                new SectionResponse(upStationId, testStationId),
                new SectionResponse(testStationId, downStationId)
        );
    }

    @Test
    @DisplayName("구간 생성시 하행역이 같을 경우 기존 구간을 변경한다.")
    void createSection2() {
        //given
        var lineResponse = lineService.createLine(lineRequest);
        var id = lineResponse.getId();
        var testStationId = insertStation("테스트3역");

        //when
        lineService.addSection(id, new SectionRequest(testStationId, downStationId, 1));

        //then
        assertThat(findSectionByLineId(id)).contains(
                new SectionResponse(testStationId, downStationId),
                new SectionResponse(upStationId, testStationId)
        );
    }

    private List<SectionResponse> findSectionByLineId(Long id) {
        var sql = "SELECT * FROM section WHERE line_id = ?";
        RowMapper<SectionResponse> sectionMapper = (rs, rowNum) -> {
            var upStationId = rs.getLong("up_station_id");
            var downStationId = rs.getLong("down_station_id");
            return new SectionResponse(upStationId, downStationId);
        };
        return jdbcTemplate.query(sql, sectionMapper, id);
    }

    @Test
    @DisplayName("구간 삭제시 상행역이 삭제되는 경우")
    void deleteSection() {
        //given
        var lineResponse = lineService.createLine(lineRequest);
        var id = lineResponse.getId();
        var testStationId = insertStation("테스트3역");
        lineService.addSection(id, new SectionRequest(testStationId, downStationId, 1));

        //when
        lineService.deleteSection(id, upStationId);

        //then
        var sectionResponse = findSectionByLineId(id);
        assertThat(sectionResponse.size()).isEqualTo(1);
        assertThat(sectionResponse.get(0).getUpStationId()).isEqualTo(testStationId);
        assertThat(sectionResponse.get(0).getDownStationId()).isEqualTo(downStationId);
    }

    @Test
    @DisplayName("구간 삭제시 중간역이 삭제되는 경우")
    void deleteSection2() {
        //given
        var lineResponse = lineService.createLine(lineRequest);
        var id = lineResponse.getId();
        var testStationId = insertStation("테스트3역");
        lineService.addSection(id, new SectionRequest(testStationId, downStationId, 1));

        //when
        lineService.deleteSection(id, testStationId);

        //then
        var sectionResponse = findSectionByLineId(id);
        assertThat(sectionResponse.size()).isEqualTo(1);
        assertThat(sectionResponse.get(0).getUpStationId()).isEqualTo(upStationId);
        assertThat(sectionResponse.get(0).getDownStationId()).isEqualTo(downStationId);
    }
}
