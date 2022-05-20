package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;

@DisplayName("최단 경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("최단 경로를 계산해서 보여준다.")
    @Test
    void showShortestPath() {
        Long seolleungId = requestPostStationAndReturnId(new StationRequest("선릉역"));
        Long sportscomplexId = requestPostStationAndReturnId(new StationRequest("종합운동장역"));
        Long seonjeongneungId = requestPostStationAndReturnId(new StationRequest("선정릉역"));
        Long samjeonId = requestPostStationAndReturnId(new StationRequest("삼전역"));

        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10));
        requestPostLine(new LineCreateRequest("수인분당선", "노란색", seolleungId, seonjeongneungId, 15));
        Long line9Id = requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5)).jsonPath()
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
        Long seolleungId = requestPostStationAndReturnId(new StationRequest("선릉역"));
        Long sportscomplexId = requestPostStationAndReturnId(new StationRequest("종합운동장역"));
        Long samjeonId = requestPostStationAndReturnId(new StationRequest("삼전역"));

        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, samjeonId, 20);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("목적지까지 도달할 수 없습니다.");
    }

    @DisplayName("출발역과 도착역이 같을 경우 경로 조회에 실패한다.")
    @Test
    void showSameSourceAndTarget() {
        Long seolleungId = requestPostStationAndReturnId(new StationRequest("선릉역"));
        Long sportscomplexId = requestPostStationAndReturnId(new StationRequest("종합운동장역"));

        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10));

        ExtractableResponse<Response> response = requestGetPath(seolleungId, seolleungId, 20);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("경로의 시작과 끝은 같은 역일 수 없습니다.");
    }

}
