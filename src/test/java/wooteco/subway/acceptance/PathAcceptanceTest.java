package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.StationRequest;

public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("경로 조회하기")
    void getPath() {
        // when 상계 중계 하계를 등록 요청한다.
        ExtractableResponse<Response> 상계역_생성_응답 = postWithBody("/stations", new StationRequest("상계역"));
        ExtractableResponse<Response> 중계역_생성_응답 = postWithBody("/stations", new StationRequest("중계역"));
        ExtractableResponse<Response> 하계역_생성_응답 = postWithBody("/stations", new StationRequest("하계역"));
        long 상계_id = 상계역_생성_응답.jsonPath().getLong("id");
        long 중계_id = 중계역_생성_응답.jsonPath().getLong("id");
        long 하계_id = 하계역_생성_응답.jsonPath().getLong("id");

        // then 역 3개 등록 요청 성공한다.
        assertAll(() -> {
            assertThat(상계역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(중계역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(하계역_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        // when 상계 중계를 종점으로 가지고 거리가 5, 추가 요금이 100원인 7호선을 등록 요청한다.
        // when 중계 하계를 종점으로 가지고 거리가 6, 추가 요금이 200원인 6호선을 등록 요청한다.
        // when 상계 하계를 중점으로 가지고 거리가 1000, 추가 요금이 50원인 5호선을 등록 요청한다.
        ExtractableResponse<Response> 칠호선_생성_응답 = postWithBody("/lines",
                new LineRequest("7호선", "red", 상계_id, 중계_id, 5, 100L));
        ExtractableResponse<Response> 육호선_생성_응답 = postWithBody("/lines",
                new LineRequest("6호선", "blue", 중계_id, 하계_id, 6, 200L));
        ExtractableResponse<Response> 오호선_생성_응답 = postWithBody("/lines",
                new LineRequest("5호선", "green", 상계_id, 하계_id, 1000, 300L));

        // then 호선 등록 요청에 성공한다.
        assertAll(() -> {
            assertThat(칠호선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(육호선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(오호선_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        });

        // when 상계 - 하계 경로 나이 20세로 경로 조회 요청을 보낸다.
        String url = "/paths?source=" + 상계_id + "&target=" + 하계_id + "&age=20";
        ExtractableResponse<Response> 경로_조회_응답 = get(url);

        // then 경로 조회 요청에 성공한다.
        // and 총 걸린 거리는 11이다.
        // and 지나온 역은 3개이다.
        // and 요금은 1550(1250 + 100 + 200)원이다.
        PathResponse pathResponse = 경로_조회_응답.body().as(PathResponse.class);
        assertAll(() -> {
            assertThat(경로_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(pathResponse.getDistance()).isEqualTo(11);
            assertThat(pathResponse.getStations().size()).isEqualTo(3);
            assertThat(pathResponse.getFare()).isEqualTo(1550L);
        });
    }
}
