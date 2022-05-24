package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;

@DisplayName("최단 경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    private Long seolleungId;
    private Long sportscomplexId;
    private Long seonjeongneungId;
    private Long samjeonId;

    @BeforeEach
    void setStations() {
        seolleungId = requestPostStationAndReturnId(new StationRequest("선릉역"));
        sportscomplexId = requestPostStationAndReturnId(new StationRequest("종합운동장역"));
        seonjeongneungId = requestPostStationAndReturnId(new StationRequest("선정릉역"));
        samjeonId = requestPostStationAndReturnId(new StationRequest("삼전역"));
    }

    @DisplayName("출발 역의 id와 도착 역의 id를 담아서 조회를 요청하면 최단 경로 정보와 200 OK를 반환한다.")
    @Test
    void showShortestPath() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10, 0));
        requestPostLine(new LineCreateRequest("수인분당선", "노란색", seolleungId, seonjeongneungId, 15, 0));
        Long line9Id = requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0)).jsonPath()
                .getLong("id");
        requestPostSection(line9Id, new SectionRequest(seonjeongneungId, sportscomplexId, 20));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(15);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350);
    }

    @DisplayName("이어져 있지 않은 경로에 대한 조회 요청을 할 경우 400 BAD REQUEST를 반환한다.")
    @Test
    void showDisconnectedPath() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("목적지까지 도달할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 같도록 조회 요청을 할 경우 400 BAD REQUEST를 반환한다.")
    @Test
    void showSameSourceAndTarget() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, seolleungId, 20);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("경로의 시작과 끝은 같은 역일 수 없습니다.");
    }

    @DisplayName("거리가 10km 이내인 경로를 조회할 경우 기본 요금을 반환한다.")
    @Test
    void showBasicFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getInt("distance")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    @DisplayName("총 거리가 10km 초과 50km 이하인 경로를 조회할 경우 5km마다 100원씩 추가된 요금을 반환한다.")
    @Test
    void showFareUnder50km() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 40, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 10, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getInt("distance")).isEqualTo(50);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2050);
    }

    @DisplayName("총 거리가 50km 초과인 경로를 조회할 경우 8km마다 100원씩 추가된 요금을 반환한다.")
    @Test
    void showFareOver50km() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 50, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 9, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getInt("distance")).isEqualTo(59);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250);
    }

    @DisplayName("추가 요금이 붙은 노선을 이용하는 경로를 조회할 경우 거리 별 운임에 추가 요금을 합산한 금액을 반환한다.")
    @Test
    void showFareWithExtraFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 500));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1750);
    }

    @DisplayName("추가 요금이 붙은 노선을 여러 개 이용하는 경로를 조회할 경우 가장 비싼 추가 요금을 운임에 합산한 금액을 반환한다.")
    @Test
    void showFareWithMostExpensiveExtraFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 500));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 900));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
    }

    @DisplayName("6세 미만의 어린이의 경로 조회를 요청할 경우 0원을 반환한다.")
    @Test
    void showBabyFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 5);

        assertThat(response.jsonPath().getInt("fare")).isEqualTo(0);
    }

    @DisplayName("6세 이상 13세 미만의 어린이의 기본 운임 경로 조회를 요청할 경우 350원을 뺀 금액의 50% 만큼을 할인받은 금액을 반환한다.")
    @Test
    void showChildrenFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 12);

        assertThat(response.jsonPath().getInt("fare")).isEqualTo(800);
    }

    @DisplayName("13세 이상 19세 미만의 청소년의 기본 운임 경로 조회를 요청할 경우 350원을 뺀 금액의 20% 만큼을 할인받은 금액을 반환한다.")
    @Test
    void showTeenagerFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 18);

        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1070);
    }

    @DisplayName("19세 이상의 성인의 기본 운임 경로 조회를 요청할 경우 기본 운임을 그대로 반환한다.")
    @Test
    void showAdultFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 19);

        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    @DisplayName("출발지 또는 도착지의 id로 1보다 작은 값이 들어간 조회 요청을 할 경우 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowStationIdLessThan1() {
        ExtractableResponse<Response> response = requestGetPath(0L, 0L, 1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("id는 1보다 작을 수 없습니다.");
    }

    @DisplayName("나이로 1보다 작은 숫자가 들어간 조회 요청을 할 경우 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowAgeLessThan1() {
        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 0);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("나이는 1보다 작을 수 없습니다.");
    }

}
