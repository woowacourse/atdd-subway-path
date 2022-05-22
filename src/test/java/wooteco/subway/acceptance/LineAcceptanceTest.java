package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.LineResponse;
import wooteco.subway.service.dto.StationRequest;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private static final String LOCATION = "Location";

    private StationRequest 하계_요청;
    private StationRequest 상계_요청;
    private ExtractableResponse<Response> 하계_응답;
    private ExtractableResponse<Response> 상계_응답;
    private long 하계_id;
    private long 상계_id;

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // given
        String lineName = "7호선";
        String lineColor = "bg-red-600";

        하계역_상계역_등록();

        //when 노선을 등록한다.
        LineRequest requestBody = new LineRequest(lineName, lineColor, 하계_id, 상계_id, 10);
        ExtractableResponse<Response> response = postWithBody("/lines", requestBody);

        // then 노선 등록에 성공한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(LOCATION)).isNotBlank();

        // and 노선 정보와 역 목록 2개를 얻을 수 있다.
        LineResponse lineResponse = response.body().as(LineResponse.class);

        assertAll(() -> {
            assertThat(lineResponse.getId()).isNotNull();
            assertThat(lineResponse.getName()).isEqualTo(lineName);
            assertThat(lineResponse.getColor()).isEqualTo(lineColor);
            assertThat(lineResponse.getStations()).hasSize(2);
        });
    }

    private void 하계역_상계역_등록() {
        // when 역 두개를 등록한다.
        하계_요청 = new StationRequest("하계역");
        하계_응답 = postWithBody("/stations", 하계_요청);

        상계_요청 = new StationRequest("상계역");
        상계_응답 = postWithBody("/stations", 상계_요청);

        하계_id = 하계_응답.jsonPath().getLong("id");
        상계_id = 상계_응답.jsonPath().getLong("id");

        //then 역 등록에 성공한다.
        assertAll(() -> {
            assertThat(하계_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(상계_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });
    }
    
    @Test
    @DisplayName("추가 요금이 존재하는 노선을 등록한다")
    void createLine_extraFare() {
        // given
        하계역_상계역_등록();

        // when 노선을 추가 요금을 포함해서 등록 요청한다.
        String name = "7호선";
        String color = "red";
        long extraFare = 100L;
        LineRequest 칠호선_요청 = new LineRequest(name, color, 상계_id, 하계_id, 10, extraFare);
        ExtractableResponse<Response> 칠호선_생성_응답 = postWithBody("/lines", 칠호선_요청);

        // then 노선 등록 요청에 성공한다.
        // and 노선 이름, 색, 역 목록 2개와 추가 요금이 요청한 바와 일치한다.
        LineResponse 칠호선_생성_응답_바디 = 칠호선_생성_응답.body().as(LineResponse.class);

        assertAll(() -> {
            assertThat(칠호선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(칠호선_생성_응답_바디.getName()).isEqualTo(name);
            assertThat(칠호선_생성_응답_바디.getColor()).isEqualTo(color);
            assertThat(칠호선_생성_응답_바디.getStations()).hasSize(2);
            assertThat(칠호선_생성_응답_바디.getExtraFare()).isEqualTo(extraFare);
        });
    }

    @Test
    @DisplayName("잘못된 값 입력해서 노선 생성 시도")
    void createLine_invalid() {
        // given
        LineRequest lineRequest = new LineRequest(null, null, null, null, null);

        // when
        ExtractableResponse<Response> response = postWithBody("/lines", lineRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("이미 존재하는 이름의 호선을 생성하려고 하면 BAD REQUEST를 반환한다.")
    void createLine_duplicatedName() {
        // given
        String lineName = "7호선";
        String redColor = "bg-red-600";
        String blueColor = "bg-blue-600";

        하계역_상계역_등록();

        LineRequest lineRequest = new LineRequest(lineName, redColor, 하계_id, 상계_id, 10);
        postWithBody("/lines", lineRequest);

        // when
        LineRequest duplicatedNameRequest = new LineRequest(lineName, blueColor, 상계_id, 하계_id, 10);
        ExtractableResponse<Response> response = postWithBody("/lines", duplicatedNameRequest);

        // then
        String bodyMessage = response.jsonPath().get("message");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(bodyMessage).isEqualTo(ExceptionMessage.DUPLICATED_LINE_NAME.getContent());
    }

    @DisplayName("모든 노선을 조회한다.")
    @Test
    void getLines() {
        하계역_상계역_등록();

        // when 노선 m개를 등록한다.
        LineRequest 칠호선_요청 = new LineRequest("7호선", "bg-green-600", 상계_id, 하계_id, 10);
        ExtractableResponse<Response> 칠호선_응답 = postWithBody("/lines", 칠호선_요청);

        LineRequest 오호선_요청 = new LineRequest("5호선", "bg-red-600", 하계_id, 상계_id, 10);
        ExtractableResponse<Response> 오호선_응답 = postWithBody("/lines", 오호선_요청);

        // then 노선 m개 등록에 성공한다.
        assertAll(() -> {
            assertThat(칠호선_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(오호선_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        // when 노선을 조회한다.
        ExtractableResponse<Response> 노선_조회_응답 = get("/lines");

        // then 노선 조회에 성공한다.
        // and 조회된 노선의 갯수는 m개다.
        assertAll(() -> {
            assertThat(노선_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(노선_조회_응답.jsonPath().getList(".")).hasSize(2);
        });
    }

    @DisplayName("특정 노선 조회한다")
    @Test
    void findById() {
        /// given
        String lineName = "7호선";
        String lineColor = "bg-green-600";

       하계역_상계역_등록();

        // when 노선 등록한다.
        LineRequest 칠호선_요청 = new LineRequest("7호선", "bg-green-600", 상계_id, 하계_id, 10);
        ExtractableResponse<Response> 칠호선_응답 = postWithBody("/lines", 칠호선_요청);

        // then 노선 등록에 성공한다.
        assertThat(칠호선_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 노선을 조회한다.
        long 칠호선_id = getIdFromLocation(칠호선_응답);
        ExtractableResponse<Response> 칠호선_조회_응답 = get("/lines/" + 칠호선_id);

        // then 노선 조회에 성공한다.
        assertThat(칠호선_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // and 조회된 노선은 등록한 노선의 이름과 동일하다.
        // and 조회된 노선에는 등록된 노선의 역 정보 2개가 있다.
        LineResponse lineResponse = 칠호선_조회_응답.body().as(LineResponse.class);
        assertAll(() -> {
            assertThat(lineResponse.getName()).isEqualTo(lineName);
            assertThat(lineResponse.getColor()).isEqualTo(lineColor);
            assertThat(lineResponse.getStations()).hasSize(2);
        });
    }

    @Test
    @DisplayName("존재하지 않은 id로 조회하면 NOT_FOUND를 반환한다.")
    void findById_invalidId() {
        // given
        long notExistsId = 1;

        // when
        ExtractableResponse<Response> response = get("/lines/" + notExistsId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("지하철 노선 정보를 수정한다.")
    void updateLine() {
        하계역_상계역_등록();

        // when 노선 등록한다.
        LineRequest 칠호선_요청 = new LineRequest("7호선", "bg-green-600", 상계_id, 하계_id, 10);
        ExtractableResponse<Response> 칠호선_응답 = postWithBody("/lines", 칠호선_요청);

        // then 노선 등록에 성공한다.
        assertThat(칠호선_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        long id = getIdFromLocation(칠호선_응답);

        // when 등록된 노선 수정 요청한다.
        LineRequest 수정_요청_바디 = new LineRequest("5호선", "bg-green-600", 하계_id, 상계_id, 10);
        ExtractableResponse<Response> 수정_응답 = putWithBody("/lines/" + id, 수정_요청_바디);

        // then 조회된 노선의 이름이 수정하려한 내용과 같다.
        // and 조회된 노선의 색깔이 수정하려한 내용과 같다.
        // and 조회된 노선의 상행 종점과 하행 종점이 수정하려한 내용과 같다.
        LineResponse 수정_역_조회 = get("/lines/"+ id).body().as(LineResponse.class);
        assertAll(() -> {
            assertThat(수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(수정_역_조회.getName()).isEqualTo(수정_요청_바디.getName());
            assertThat(수정_역_조회.getColor()).isEqualTo(수정_요청_바디.getColor());
            assertThat(수정_역_조회.getStations().get(0).getId()).isEqualTo(수정_요청_바디.getUpStationId());
            assertThat(수정_역_조회.getStations().get(1).getId()).isEqualTo(수정_요청_바디.getDownStationId());
        });
    }

    @Test
    @DisplayName("지하철 노선 정보를 삭제한다.")
    void deleteLine() {
        하계역_상계역_등록();

        // when 노선을 등록한다.
        LineRequest 칠호선_요청 = new LineRequest("7호선", "red", 상계_id, 하계_id, 10);
        ExtractableResponse<Response> 칠호선_생성_응답 = postWithBody("/lines", 칠호선_요청);

        // then 노선 등록에 성공한다.
        assertThat(칠호선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 등록된 노선을 삭제 요청한다.
        long id = getIdFromLocation(칠호선_생성_응답);
        ExtractableResponse<Response> 칠호선_삭제_응답 = delete("/lines/" + id);

        // then 노선 삭제에 성공한다.
        assertThat(칠호선_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
