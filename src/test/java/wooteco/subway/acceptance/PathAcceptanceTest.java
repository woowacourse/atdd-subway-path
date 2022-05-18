package wooteco.subway.acceptance;

import static wooteco.subway.acceptance.util.RestAssuredUtils.createData;
import static wooteco.subway.acceptance.util.RestAssuredUtils.getLocationId;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.SectionRequest;

class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void getPaths() {
        ExtractableResponse<Response> createStation1 = createData("/stations",
                new Station("새로운상행종점"));
        ExtractableResponse<Response> createStation2 = createData("/stations",
                new Station("기존상행종점"));
        ExtractableResponse<Response> createStation3 = createData("/stations",
                new Station("기존하행종점"));
        final LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600",
                getLocationId(createStation2), getLocationId(createStation3), 100);
        ExtractableResponse<Response> lineResponse = createData("/lines", lineRequest);

        // when
        final SectionRequest sectionRequest = new SectionRequest(getLocationId(createStation1),
                getLocationId(createStation2), 50);
        final ExtractableResponse<Response> sectionResponse
                = createData("/lines/" + getLocationId(lineResponse) + "/sections", sectionRequest);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", 1)
                .queryParam("target", 3)
                .queryParam("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    @DisplayName("환승을 포함한 지하철 최단 거리를 조회한다.")
    @Test
    void getPathWithTransferLine() {
        ExtractableResponse<Response> 성수 = createData("/stations", new Station("성수"));
        ExtractableResponse<Response> 건대입구 = createData("/stations", new Station("건대입구"));
        ExtractableResponse<Response> 강남구청 = createData("/stations", new Station("강남구청"));

        final LineRequest lineRequest = new LineRequest("2호선", "bg-red-600", getLocationId(성수),
                getLocationId(건대입구), 100);
        final LineRequest lineRequest2 = new LineRequest("9호선", "bg-blue-600", getLocationId(건대입구),
                getLocationId(강남구청), 100);

        createData("/lines", lineRequest);
        createData("/lines", lineRequest2);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", getLocationId(성수))
                .queryParam("target", getLocationId(강남구청))
                .queryParam("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    @DisplayName("환승을 포함한 지하철 최단 거리가 여러개일 때, 모두 조회한다.")
    @Test
    void getPathsWithTransferLine() {
        ExtractableResponse<Response> 성수 = createData("/stations", new Station("성수"));
        ExtractableResponse<Response> 건대입구 = createData("/stations", new Station("건대입구"));
        ExtractableResponse<Response> 강남구청 = createData("/stations", new Station("강남구청"));

        final LineRequest lineRequest = new LineRequest("2호선", "bg-red-600", getLocationId(성수),
                getLocationId(건대입구), 10);
        final LineRequest lineRequest2 = new LineRequest("9호선", "bg-blue-600", getLocationId(건대입구),
                getLocationId(강남구청), 10);

        createData("/lines", lineRequest);
        createData("/lines", lineRequest2);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("source", getLocationId(성수))
                .queryParam("target", getLocationId(강남구청))
                .queryParam("age", 20)
                .when()
                .get("/paths")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
