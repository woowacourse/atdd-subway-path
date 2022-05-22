package wooteco.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.StationRequest;

import static org.hamcrest.Matchers.*;

@SuppressWarnings("NonAsciiCharacters")
public class StationAcceptanceTest extends AcceptanceTest {

    private final String basicPath = "/stations";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        StationRequest request = new StationRequest("강남역");

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", response -> equalTo("/stations/" + response.path("id")));
    }

    @DisplayName("비어있는 값으로 이름을 생성하면 400번 코드를 반환한다.")
    @Test
    void createStationWithInvalidDataSize() {
        StationRequest request = new StationRequest("");

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성하면 400번 코드를 반환한다.")
    @Test
    void createStationWithDuplicateName() {
        RestAssuredConvenienceMethod.postRequest(new StationRequest("선릉역"), basicPath);
        StationRequest request = new StationRequest("선릉역");

        RestAssuredConvenienceMethod.postRequest(request, basicPath)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        Long 선릉역_id = RestAssuredConvenienceMethod.postRequestAndGetId(new StationRequest("선릉역"), basicPath);
        Long 선정릉역_id = RestAssuredConvenienceMethod.postRequestAndGetId(new StationRequest("선정릉역"), basicPath);

        RestAssuredConvenienceMethod.getRequest(basicPath)
                .statusCode(HttpStatus.OK.value())
                .body("", allOf(
                        hasItem(both(hasEntry("id", 1)).and(hasEntry("name", "선릉역"))),
                        hasItem(both(hasEntry("id", 2)).and(hasEntry("name", "선정릉역")))
                ));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        Long 선릉역_id = RestAssuredConvenienceMethod.postRequestAndGetId(new StationRequest("선릉역"), basicPath);

        RestAssuredConvenienceMethod.deleteRequest("/stations/" + 선릉역_id)
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 지하철역을 삭제하려하면 400번 코드를 반환한다.")
    @Test
    void deleteStationWithNotExistData() {
        RestAssuredConvenienceMethod.deleteRequest("/stations/" + 100L)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에 속해있는 지하철역을 삭제하려하면 400번 코드를 반환한다.")
    @Test
    void deleteStationWithStationInLine() {
        Long 선릉역_id = RestAssuredConvenienceMethod.postRequestAndGetId(new StationRequest("선릉역"), "/stations");
        Long 선정릉역_id = RestAssuredConvenienceMethod.postRequestAndGetId(new StationRequest("선정릉역"), "/stations");
        RestAssuredConvenienceMethod.postRequestAndGetId(
                new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 10, 0), "/lines");

        RestAssuredConvenienceMethod.deleteRequest("/stations/" + 선릉역_id)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
