package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineResponse;
import wooteco.subway.service.dto.StationResponse;
import wooteco.subway.ui.dto.LineCreateRequest;
import wooteco.subway.ui.dto.LineRequest;
import wooteco.subway.ui.dto.SectionRequest;
import wooteco.subway.utils.RestAssuredUtil;

@DisplayName("지하철 노선 관련 기능 - LineAcceptanceTest")
public class LineAcceptanceTest extends AcceptanceTest {

    private Long stationId1;
    private Long stationId2;
    private Long stationId3;
    private Long lineId1;
    private Long lineId2;
    private Long sectionId;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @BeforeEach
    void init() {
        stationId1 = stationDao.save(new Station("강남역"));
        stationId2 = stationDao.save(new Station("왕십리역"));
        stationId3 = stationDao.save(new Station("정자역"));

        lineId1 = lineDao.save(new LineCreateRequest("신분당선", "bg-red-600", stationId1, stationId2, 10, 10));
        lineId2 = lineDao.save(new LineCreateRequest("분당선", "bg-green-600", stationId1, stationId2, 10, 10));

        sectionId = sectionDao.save(new Section(lineId1, stationId1, stationId2, 5));
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", "bg-green-500", stationId1, stationId2, 20, 0);

        // when
        ExtractableResponse<Response> response = RestAssuredUtil.post("/lines", lineCreateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("잘못된 지하철 노선 생성")
    @ParameterizedTest(name = "{0}")
    @MethodSource("parameterProvider")
    void createLineFailed(String displayName, LineCreateRequest lineCreateRequest) {
        // given

        // when
        ExtractableResponse<Response> response = RestAssuredUtil.post("/lines", lineCreateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
                Arguments.arguments(
                        "이미 존재하는 노선 이름으로 생성",
                        new LineCreateRequest("신분당선", "bg-red-600", 1L, 2L, 10, 0)
                ),
                Arguments.arguments(
                        "구간 거리가 음수",
                        new LineCreateRequest("2호선", "bg-green-500", 1L, 2L, -10, 0)
                ),
                Arguments.arguments(
                        "구간 거리가 0",
                        new LineCreateRequest("2호선", "bg-green-500", 1L, 2L, 0, 0)
                ),
                Arguments.arguments(
                        "존재하지 않는 역 등록 시도",
                        new LineCreateRequest("2호선", "bg-green-500", 1L, 6L, 10, 0)
                )
        );
    }

    @DisplayName("노선 목록을 조회한다.")
    @Test
    void getLines() {
        /// given
        List<Long> expectedLineIds = List.of(lineId1, lineId2);

        // when
        ExtractableResponse<Response> response = RestAssuredUtil.get("/lines");
        List<Long> resultLineIds = findLineIds(response);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private List<Long> findLineIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
    }

    @DisplayName("id 를 이용하여 노선을 조회한다.")
    @Test
    void findLine() {
        //given
        String id = String.valueOf(lineId1);

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.get("/lines/" + id);

        //then
        Integer findId = response.jsonPath().get("id");
        assertThat(findId).isEqualTo(Integer.parseInt(id));
    }

    @DisplayName("존재하지 않는 id 를 이용하여 노선을 조회한다.")
    @Test
    void findLineWithNoneId() {
        //given
        String id = "999999";

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.get("/lines/" + id);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("id 를 이용하여 노선을 수정한다.")
    @Test
    void updateLine() {
        //given
        String id = String.valueOf(lineId1);

        //when
        String name = "다른분당선";
        LineRequest lineRequest = new LineRequest(name, "bg-red-600", 0);

        ExtractableResponse<Response> response = RestAssuredUtil.put("/lines/" + id, lineRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("노선을 중복된 이름으로 수정한다.")
    @Test
    void updateLineWithDuplicatedName() {
        //given
        String id = String.valueOf(lineId1);

        //when
        String name = "분당선";
        LineRequest lineRequest = new LineRequest(name, "bg-red-600", 0);

        ExtractableResponse<Response> response = RestAssuredUtil.put("/lines/" + id, lineRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("id 를 이용하여 노선을 삭제한다.")
    @Test
    void deleteLineById() {
        //given
        List<Long> expectedIds = selectLines();
        expectedIds.remove(lineId1);

        //when
        RestAssuredUtil.delete("/lines/" + lineId1, new HashMap<>());

        //then
        assertThat(expectedIds).isEqualTo(selectLines());
    }

    private List<Long> selectLines() {
        ExtractableResponse<Response> response = RestAssuredUtil.get("/lines");
        return findLineIds(response);
    }

    @DisplayName("구간 생성")
    @Test
    void createSection() {
        // given
        SectionRequest sectionRequest = new SectionRequest(stationId2, stationId3, 5);
        String url = "/lines/" + lineId1 + "/sections";

        // when
        RestAssuredUtil.post(url, sectionRequest);

        // then
        List<StationResponse> stations = findStations(lineId1);

        assertThat(stations).extracting("id", "name")
                .containsExactly(
                        tuple(stationId1, "강남역"),
                        tuple(stationId2, "왕십리역"),
                        tuple(stationId3, "정자역")
                );
    }

    @DisplayName("구간 삭제")
    @Test
    void deleteSection() {
        // given
        Map<String, String> source = new HashMap<>();
        source.put("stationId", sectionId.toString());
        String url = "/lines/" + lineId1 + "/sections";

        // when
        RestAssuredUtil.delete(url, source);

        // then
        List<Long> stationIds = findStations(lineId1)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).doesNotContain(sectionId);
    }

    private List<StationResponse> findStations(Long lineId) {
        ExtractableResponse<Response> findLine = RestAssuredUtil.get("/lines/" + lineId);
        return findLine.jsonPath().getList("stations", StationResponse.class);
    }
}
