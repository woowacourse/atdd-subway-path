package wooteco.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.exception.dto.ExceptionResponse;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static wooteco.subway.line.SectionAcceptanceTest.지하철_구간_등록되어_있음;
import static wooteco.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 혜화역;
    private StationResponse 한성대입구역;
    private StationResponse 남부터미널역;

    public static ExtractableResponse<Response> 거리_경로_조회_요청(long source, long target) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", source)
                .queryParam("target", target)
                .when().get("/api/paths")
                .then().log().all()
                .extract();
    }

    public static void 적절한_경로_응답됨(ExtractableResponse<Response> response, ArrayList<StationResponse> expectedPath) {
        PathResponse pathResponse = response.as(PathResponse.class);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedPathIds = expectedPath.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedPathIds);
    }

    public static void 총_거리가_응답됨(ExtractableResponse<Response> response, int totalDistance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
    }

    /**             10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*  3                *신분당선* 10
     * |                2       |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");
        혜화역 = 지하철역_등록되어_있음("혜화역");
        한성대입구역 = 지하철역_등록되어_있음("한성대입구역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-400", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-300", 교대역, 양재역, 5);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-200", 혜화역, 한성대입구역, 100);

        지하철_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistance() {
        //when
        ExtractableResponse<Response> response1 = 거리_경로_조회_요청(3L, 2L);
        ExtractableResponse<Response> response2 = 거리_경로_조회_요청(1L, 4L);

        //then
        적절한_경로_응답됨(response1, Lists.newArrayList(교대역, 남부터미널역, 양재역));
        총_거리가_응답됨(response1, 5);
        적절한_경로_응답됨(response2, Lists.newArrayList(강남역, 양재역, 남부터미널역));
        총_거리가_응답됨(response2, 12);
    }

    @DisplayName("존재하지 않는 역으로 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceWithNotExistStation() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 6L);

        //then
        경로_조회에_실패함(response, "해당 경로를 찾을 수 없습니다.");
    }

    @DisplayName("동일한 역 2개로 최단 거리 경로를 조회한다.")
    @Test
    void findPathByDistanceWithSameStation() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(1L, 1L);

        //then
        경로_조회에_실패함(response, "출발역과 도착역을 다르게 해서 경로를 조회해주세요.");
    }

    private void 경로_조회에_실패함(ExtractableResponse<Response> response, String errorMessage) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(exceptionResponse.getMessage()).isEqualTo(errorMessage);
    }


}
