package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
import wooteco.subway.dto.line.LineResponse;
import wooteco.subway.dto.station.StationResponse;

@JdbcTest
class LineServiceTest {

    private static final String LINE_NAME = "테스트1호선";
    private static final String LINE_COLOR = "테스트1색";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private LineService lineService;
    private Long lineId;

    @BeforeEach
    void setUp() {
        lineService = new LineService(
                new LineDao(jdbcTemplate),
                new SectionDao(jdbcTemplate),
                new StationDao(jdbcTemplate)
        );

        var upStationId = insertStation("테스트1역");
        var downStationId = insertStation("테스트2역");
        var lineRequest = new LineRequest(LINE_NAME, LINE_COLOR, upStationId, downStationId, 2, 0);

        var lineResponse = lineService.create(lineRequest);

        lineId = lineResponse.getId();
    }

    private Long insertStation(String name) {
        var sql = "INSERT INTO station (name) values(?)";

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var statement = con.prepareStatement(sql, new String[]{"id"});
            statement.setString(1, name);
            return statement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Test
    void createLine() {
        //given
        var station3Id = insertStation("테스트3역");
        var station4Id = insertStation("테스트4역");
        var lineRequest = new LineRequest("테스트2호선", "테스트2색", station3Id, station4Id, 2, 0);

        //when
        var lineResponse = lineService.create(lineRequest);

        //then
        var stationNames = lineResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(lineResponse.getName()).isEqualTo("테스트2호선"),
                () -> assertThat(lineResponse.getColor()).isEqualTo("테스트2색"),
                () -> assertThat(stationNames).contains("테스트3역", "테스트4역")
        );
    }

    @Test
    @DisplayName("노선 조회")
    void findLineByLineId() {
        //when
        var findLineResponse = lineService.find(lineId);

        //then
        var stationNames = findLineResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(findLineResponse.getName()).isEqualTo(LINE_NAME),
                () -> assertThat(findLineResponse.getColor()).isEqualTo(LINE_COLOR),
                () -> assertThat(stationNames).contains("테스트1역", "테스트2역")
        );
    }

    @Test
    @DisplayName("노선 목록 조회")
    void findAll() {
        //given
        var station3Id = insertStation("테스트3역");
        var station4Id = insertStation("테스트4역");
        var lineRequest = new LineRequest("테스트2호선", "테스트2색", station3Id, station4Id, 2, 0);
        lineService.create(lineRequest);

        //when
        var lineResponses = lineService.findAll();

        //then
        var lineNames = lineResponses.stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());
        var lineColors = lineResponses.stream()
                .map(LineResponse::getColor)
                .collect(Collectors.toList());
        var stationsNames = lineResponses.stream()
                .map(LineResponse::getStations)
                .flatMap(Collection::stream)
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(lineNames).contains(LINE_NAME, "테스트2호선"),
                () -> assertThat(lineColors).contains(LINE_COLOR, "테스트2색"),
                () -> assertThat(stationsNames).contains("테스트1역", "테스트2역", "테스트3역", "테스트4역")
        );
    }

    @Test
    void updateLine() {
        //given
        var lineRequest = new LineRequest("테스트2호선", "테스트2색", 0);

        //when
        lineService.update(lineId, lineRequest);

        //then
        var actual = findLine().get(0);

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("테스트2호선"),
                () -> assertThat(actual.getColor()).isEqualTo("테스트2색")
        );
    }

    private List<LineResponse> findLine() {
        var sql = "SELECT * FROM line";

        RowMapper<LineResponse> rowMapper = (rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var color = rs.getString("color");
            var extraFare = rs.getInt("extraFare");
            return new LineResponse(id, name, color, extraFare);
        };

        return jdbcTemplate.query(sql, rowMapper);
    }

    @Test
    @DisplayName("노선 삭제")
    void deleteLine() {
        //when
        lineService.deleteById(lineId);

        //then
        assertThat(findLine().isEmpty()).isTrue();
    }
}
