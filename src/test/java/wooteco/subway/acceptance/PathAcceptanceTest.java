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

    @DisplayName("최단 경로를 계산해서 보여준다.")
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

    @DisplayName("이어져 있지 않은 경로를 조회할 경우 조회에 실패한다.")
    @Test
    void showDisconnectedPath() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("목적지까지 도달할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 같을 경우 경로 조회에 실패한다.")
    @Test
    void showSameSourceAndTarget() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, seolleungId, 20);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("경로의 시작과 끝은 같은 역일 수 없습니다.");
    }

    @DisplayName("거리가 10km 이내인 경우 기본 요금이 발생한다.")
    @Test
    void showBasicFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1250);
    }

    @DisplayName("총 거리가 10km 초과 50km 이하인 경우 5km마다 100원씩 추가되어 계산한다.")
    @Test
    void showFareUnder50km() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 40, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 10, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(50);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2050);
    }

    @DisplayName("총 거리가 50km 초과인 경우 8km마다 100원씩 추가되어 계산한다.")
    @Test
    void showFareOver50km() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 50, 0));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 9, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(59);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2250);
    }

    @DisplayName("추가 요금이 붙은 노선을 이용할 경우 운임에 합산해서 계산한다.")
    @Test
    void showFareWithExtraFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 500));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 0));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1750);
    }

    @DisplayName("추가 요금이 붙은 노선을 여러 개 이용할 경우 가장 비싼 추가 요금을 운임에 합산해서 계산한다.")
    @Test
    void showFareWithMostExpensiveExtraFare() {
        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 5, 500));
        requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5, 900));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(10);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(2150);
    }

}
