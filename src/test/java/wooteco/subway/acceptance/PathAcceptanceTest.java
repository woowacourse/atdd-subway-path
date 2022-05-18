package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;

@DisplayName("최단 경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("최단 경로를 계산해서 보여준다.")
    @Test
    void showShortestPath() {
        // given
        Long seolleungId = requestPostStationAndReturnId(new StationRequest("선릉역"));
        Long sportscomplexId = requestPostStationAndReturnId(new StationRequest("종합운동장역"));
        Long seonjeongneungId = requestPostStationAndReturnId(new StationRequest("선정릉역"));
        Long samjeonId = requestPostStationAndReturnId(new StationRequest("삼전역"));

        requestPostLine(new LineCreateRequest("2호선", "초록색", seolleungId, sportscomplexId, 10));
        requestPostLine(new LineCreateRequest("수인분당선", "노란색", seolleungId, seonjeongneungId, 15));
        Long line9Id = requestPostLine(new LineCreateRequest("9호선", "금색", sportscomplexId, samjeonId, 5)).jsonPath()
                .getLong("id");
        requestPostSection(line9Id, new SectionRequest(seonjeongneungId, sportscomplexId, 20));

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/paths?source=" + seolleungId + "&target=" + samjeonId + "&age=20")
                .then().log().all()
                .extract();
        ;

        // then
        assertThat(response.jsonPath().getList("stations")).extracting("name")
                .containsExactly("선릉역", "종합운동장역", "삼전역");
        assertThat(response.jsonPath().getInt("distance")).isEqualTo(15);
        assertThat(response.jsonPath().getInt("fare")).isEqualTo(1350);
    }
}
