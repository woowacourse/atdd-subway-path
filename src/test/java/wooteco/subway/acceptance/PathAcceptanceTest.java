package wooteco.subway.acceptance;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static wooteco.subway.acceptance.util.RestAssuredUtils.checkProperResponseStatus;
import static wooteco.subway.acceptance.util.RestAssuredUtils.createData;
import static wooteco.subway.acceptance.util.RestAssuredUtils.getData;
import static wooteco.subway.acceptance.util.RestAssuredUtils.getLocationId;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.SectionRequest;

class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void getPaths() {
        // given
        ExtractableResponse<Response> 성수 = createData("/stations", new Station("성수"));
        ExtractableResponse<Response> 건대입구 = createData("/stations", new Station("건대입구"));
        ExtractableResponse<Response> 강남구청 = createData("/stations", new Station("강남구청"));

        final LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", getLocationId(건대입구), getLocationId(강남구청), 100);
        ExtractableResponse<Response> lineResponse = createData("/lines", lineRequest);
        createData("/lines/" + getLocationId(lineResponse) + "/sections", new SectionRequest(getLocationId(성수), getLocationId(건대입구), 50));

        // when
        final String url = "/paths?source=" + getLocationId(성수) + "&target=" + getLocationId(강남구청) + "&age=20";
        ExtractableResponse<Response> response = getData(url);

        // then
        checkProperResponseStatus(response, HttpStatus.OK);
        checkProperData(url,
                new Station(getLocationId(성수), "성수"),
                new Station(getLocationId(건대입구), "건대입구"),
                new Station(getLocationId(강남구청), "강남구청"));

    }

    @DisplayName("환승을 포함한 지하철 최단 거리를 조회한다.")
    @Test
    void getPathWithTransferLine() {
        // given
        ExtractableResponse<Response> 성수 = createData("/stations", new Station("성수"));
        ExtractableResponse<Response> 건대입구 = createData("/stations", new Station("건대입구"));
        ExtractableResponse<Response> 강남구청 = createData("/stations", new Station("강남구청"));

        createData("/lines", new LineRequest("2호선", "bg-red-600", getLocationId(성수), getLocationId(건대입구), 100));
        createData("/lines", new LineRequest("9호선", "bg-blue-600", getLocationId(건대입구), getLocationId(강남구청), 100));

        // when
        final String url = "/paths?source=" + getLocationId(성수) + "&target=" + getLocationId(강남구청) + "&age=20";
        ExtractableResponse<Response> response = getData(url);

        // then
        checkProperResponseStatus(response, HttpStatus.OK);
        checkProperData(url,
                new Station(getLocationId(성수), "성수"),
                new Station(getLocationId(건대입구), "건대입구"),
                new Station(getLocationId(강남구청), "강남구청"));
    }

    private void checkProperData(String url, Station station1, Station station2, Station station3) {
        get(url).then()
                .assertThat()
                .body("stations[0].id", equalTo(station1.getId().intValue()))
                .body("stations[0].name", equalTo(station1.getName()))
                .body("stations[1].id", equalTo(station2.getId().intValue()))
                .body("stations[1].name", equalTo(station2.getName()))
                .body("stations[2].id", equalTo(station3.getId().intValue()))
                .body("stations[2].name", equalTo(station3.getName()));
    }
}
