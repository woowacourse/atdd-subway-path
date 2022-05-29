package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.acceptance.util.RestAssuredUtils.checkProperResponseStatus;
import static wooteco.subway.acceptance.util.RestAssuredUtils.createData;
import static wooteco.subway.acceptance.util.RestAssuredUtils.getDataWithParameter;
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

    private final Station 성수역 = new Station("성수");
    private final Station 건대입구역 = new Station("건대입구");
    private final Station 강남구청역 = new Station("강남구청");

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void getPaths() {
        // given
        ExtractableResponse<Response> 성수 = createData("/stations", 성수역);
        ExtractableResponse<Response> 건대입구 = createData("/stations", 건대입구역);
        ExtractableResponse<Response> 강남구청 = createData("/stations", 강남구청역);

        final LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", getLocationId(건대입구), getLocationId(강남구청), 100);
        ExtractableResponse<Response> lineResponse = createData("/lines", lineRequest);
        createData("/lines/" + getLocationId(lineResponse) + "/sections", new SectionRequest(getLocationId(성수), getLocationId(건대입구), 50));

        // when
        final String url = "/paths";
        ExtractableResponse<Response> response = getDataWithParameter(url, getLocationId(성수), getLocationId(강남구청), 20);
        response.jsonPath().getList("stations");

        // then
        checkProperResponseStatus(response, HttpStatus.OK);
        checkProperData(response,
                new Station(getLocationId(성수), "성수"),
                new Station(getLocationId(건대입구), "건대입구"),
                new Station(getLocationId(강남구청), "강남구청"));

    }

    @DisplayName("환승을 포함한 지하철 최단 거리를 조회한다.")
    @Test
    void getPathWithTransferLine() {
        // given
        ExtractableResponse<Response> 성수 = createData("/stations", 성수역);
        ExtractableResponse<Response> 건대입구 = createData("/stations", 건대입구역);
        ExtractableResponse<Response> 강남구청 = createData("/stations", 강남구청역);

        createData("/lines", new LineRequest("2호선", "bg-red-600", getLocationId(성수), getLocationId(건대입구), 100));
        createData("/lines", new LineRequest("9호선", "bg-blue-600", getLocationId(건대입구), getLocationId(강남구청), 100));

        // when
        final String url = "/paths";
        ExtractableResponse<Response> response = getDataWithParameter(url, getLocationId(성수), getLocationId(강남구청), 20);

        // then
        checkProperResponseStatus(response, HttpStatus.OK);
        checkProperData(response,
                new Station(getLocationId(성수), "성수"),
                new Station(getLocationId(건대입구), "건대입구"),
                new Station(getLocationId(강남구청), "강남구청"));
    }

    private void checkProperData(ExtractableResponse<Response> response, Station station1, Station station2, Station station3) {
        assertThat(response.jsonPath().getList("stations.id")).contains(station1.getId().intValue(), station2.getId().intValue(), station3.getId().intValue());
        assertThat(response.jsonPath().getList("stations.name")).contains(station1.getName(), station2.getName(), station3.getName());
    }
}
