package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.service.dto.StationRequest;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성 시나리오")
    @Test
    void createStation() {
        // when 지하철 역 생성 요청한다.
        StationRequest body = new StationRequest("노원역");

        ExtractableResponse<Response> response = postWithBody("/stations", body);

        // then 지하철 역 생성 성공한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성시 BAD REQUEST를 반환한다.")
    @Test
    void createStationWithDuplicateName() {
        // given 역 생성 요청을 한다.
        StationRequest requestBody = new StationRequest("강남역");

        postWithBody("/stations", requestBody);

        // when 동일한 역 생성 요청을 한다.
        ExtractableResponse<Response> duplicatedNameResponse = postWithBody("/stations", requestBody);

        // then 역 생성에 실패한다.
        assertThat(duplicatedNameResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // when 지하철 역을 2개 생성 요청한다.
        StationRequest 강남역_생성_바디 = new StationRequest("강남역");
        ExtractableResponse<Response> 강남역_생성_응답 = postWithBody("/stations", 강남역_생성_바디);

        StationRequest 역삼역_생성_바디 = new StationRequest("역삼역");
        ExtractableResponse<Response> 역삼역_생성_응답 = postWithBody("/stations", 역삼역_생성_바디);

        // then 지하철 역 생성에 성공한다.
        assertAll(() -> {
            assertThat(강남역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(역삼역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        // when 지하철 역 목록 조회 요청한다.
        ExtractableResponse<Response> 저장된_역_조회_응답 = get("/stations");

        // then 지하철 역 목록 조회 요청에 성공한다.
        assertThat(저장된_역_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        // and 지하철 역 목록 갯수는 2개다.
        assertThat(저장된_역_조회_응답.jsonPath().getList(".")).hasSize(2);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // when 지하철 역 등록 요청한다.
        StationRequest requestBody = new StationRequest("노원역");
        ExtractableResponse<Response> 생성_응답 = postWithBody("/stations", requestBody);

        // then 역 등록에 성공한다.
        assertThat(생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 등록한 역을 삭제 요청한다.
        String uri = 생성_응답.header("Location");
        ExtractableResponse<Response> response = delete(uri);

        // then 지하철 역 삭제 성공한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
