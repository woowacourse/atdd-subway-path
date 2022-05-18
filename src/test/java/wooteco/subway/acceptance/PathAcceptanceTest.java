package wooteco.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
public class PathAcceptanceTest extends AcceptanceTest {

    private Long 선릉역_id;
    private Long 선정릉역_id;
    private Long 한티역_id;
    private Long 모란역_id;
    private Long 기흥역_id;
    private Long 강남역_id;

    @BeforeEach
    void setUpStations() {
        선릉역_id = RestAssuredConvenienceMethod.postStationAndGetId(new StationRequest("선릉역"), "/stations");
        선정릉역_id = RestAssuredConvenienceMethod.postStationAndGetId(new StationRequest("선정릉역"), "/stations");
        한티역_id = RestAssuredConvenienceMethod.postStationAndGetId(new StationRequest("한티역"), "/stations");
        모란역_id = RestAssuredConvenienceMethod.postStationAndGetId(new StationRequest("모란역"), "/stations");
        기흥역_id = RestAssuredConvenienceMethod.postStationAndGetId(new StationRequest("기흥역"), "/stations");
        강남역_id = RestAssuredConvenienceMethod.postStationAndGetId(new StationRequest("강남역"), "/stations");

        Long 분당선_id = RestAssuredConvenienceMethod.postLineAndGetId(
                new LineRequest("분당선", "yellow", 선릉역_id, 선정릉역_id, 50), "/lines");
        RestAssuredConvenienceMethod.postRequest(new SectionRequest(선정릉역_id, 한티역_id, 8), "/lines/" + 분당선_id + "/sections");
        RestAssuredConvenienceMethod.postRequest(new SectionRequest(한티역_id, 강남역_id, 20), "/lines/" + 분당선_id + "/sections");
        Long 신분당선_id = RestAssuredConvenienceMethod.postLineAndGetId(
                new LineRequest("신분당선", "red", 모란역_id, 선정릉역_id, 6), "/lines");
        Long 우테코선_id = RestAssuredConvenienceMethod.postLineAndGetId(
                new LineRequest("우테코선", "blue", 기흥역_id, 모란역_id, 10), "/lines");
        RestAssuredConvenienceMethod.postRequest(new SectionRequest(모란역_id, 강남역_id, 5), "/lines/" + 우테코선_id + "/sections");
    }

    @DisplayName("올바른 경로와 요금을 가져오는지 테스트한다.")
    @Test
    void findPath() {
        String uri = pathRequestFormat(기흥역_id, 강남역_id);

        ExtractableResponse<Response> request = RestAssuredConvenienceMethod.getRequest(uri);

        assertThat(request.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("존재하지 않는 역의 경로를 찾을때는 400 코드가 반환된다.")
    @Test
    void findPathWithNotExistStation() {
        String uri = pathRequestFormat(기흥역_id, 100L);

        ExtractableResponse<Response> request = RestAssuredConvenienceMethod.getRequest(uri);

        assertThat(request.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private String pathRequestFormat(Long from, Long to) {
        return String.format("/paths?source=%d&target=%d&age=15", from, to);
    }
}
