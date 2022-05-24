package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.BodyCreator.createStation;
import static wooteco.subway.acceptance.BodyCreator.makeBodyForPost;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("구간을 생성한다.")
    @Test
    void saveSection() {
        createStation("강남역");
        createStation("선릉역");
        createStation("잠실역");

        ExtractableResponse<Response> createLineResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.post(
                makeBodyForPost("2", "3", "10"),
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections"
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("구간 저장 시 요청 객체의 요청 정보를 누락(공백, null)하면 에러를 응답한다.")
    @Test
    void saveSectionMissingParam() {
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createLineResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.post(
                makeBodyForPost("", "", ""),
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections"
        );

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).contains("upStationId", "downStationId", "distance")
        );
    }

    @DisplayName("등록되지 않은 id의 자하철 노선에 등록 요청한다.(404에러)")
    @Test
    void saveSection_withNotExistLineId() {
        createStation("강남역");
        createStation("선릉역");
        createStation("잠실역");

        RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.post(
                makeBodyForPost("2", "3", "10"),
                "/lines/" + 2 + "/sections"
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("거리가 1이상이 아닌 경우 자하철 노선 등록을 할 수 없다.(400에러)")
    @Test
    void saveSectionWithWrongDistance() {
        createStation("강남역");
        createStation("선릉역");
        createStation("잠실역");

        RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.post(
                makeBodyForPost("2", "3", "-1"),
                "/lines/" + 1 + "/sections"
        );

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().asString()).contains("구간 사이의 거리는 양수여야합니다.")
        );
    }

    @DisplayName("상행역, 하행역이 이미 노선에 있는 구간을 등록 요청한다.(400에러)")
    @Test
    void saveSection_withAlreadyExistSection() {
        createStation("강남역");
        createStation("선릉역");
        createStation("잠실역");

        ExtractableResponse<Response> createLineResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        RequestFrame.post(
                makeBodyForPost("2", "3", "10"),
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections"
        );

        ExtractableResponse<Response> response = RequestFrame.post(
                makeBodyForPost("1", "3", "20"),
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections"
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("상행역, 하행역이 둘다 노선에 없는 구간을 등록 요청한다.(404에러)")
    @Test
    void saveSection_withNotExistUpAndDownStationBoth() {
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createLineResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "3");
        params.put("downStationId", "4");
        params.put("distance", "10");

        ExtractableResponse<Response> response = RequestFrame.post(
                makeBodyForPost("3", "4", "10"),
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections"
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("기존 구간의 내부에 더 긴 구간을 등록 요청한다.(400에러)")
    @Test
    void saveSection_withBiggerInnerSection() {
        createStation("강남역");
        createStation("선릉역");
        createStation("잠실역");

        ExtractableResponse<Response> createLineResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.post(
                makeBodyForPost("1", "3", "10"),
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections"
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("구간을 삭제한다.")
    @Test
    void deleteSection() {
        createStation("강남역");
        createStation("선릉역");
        createStation("잠실역");

        ExtractableResponse<Response> createLineResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        RequestFrame.post(
                makeBodyForPost("2", "3", "10"),
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections"
        );

        ExtractableResponse<Response> response = RequestFrame.delete(
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections?stationId=2");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("노선에 구간이 한개인 경우 구간 삭제 요청한다.(400에러)")
    @Test
    void deleteSection_justOneSectionExistsInLine() {
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createLineResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.delete(
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections?stationId=2");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선이 지나지 않는 역을 포함한 구간 삭제 요청한다.(404에러)")
    @Test
    void deleteSection_notExistStationInLine() {
        createStation("강남역");
        createStation("선릉역");

        ExtractableResponse<Response> createLineResponse = RequestFrame.post(
                BodyCreator.makeLineBodyForPost("2호선", "green", "1", "2", "10", "900"),
                "/lines"
        );

        ExtractableResponse<Response> response = RequestFrame.delete(
                "/lines/" + createLineResponse.jsonPath().getLong("id") + "/sections?stationId=3");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
