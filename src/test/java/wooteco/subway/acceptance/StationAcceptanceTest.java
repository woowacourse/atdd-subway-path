package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.delete;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.get;
import static wooteco.subway.acceptance.fixture.HttpRequestUtil.post;
import static wooteco.subway.acceptance.fixture.TestLine.LINE_2;
import static wooteco.subway.acceptance.fixture.TestStation.GANGNAM;
import static wooteco.subway.acceptance.fixture.TestStation.YEOKSAM;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("올바른 생성 요청으로 지하철 역을 생성하면 201 CREATED와 역 리소스 주소를 반환한다.")
    @Test
    void createStation() {
        ExtractableResponse<Response> response = GANGNAM.requestSave();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역 생성을 요청하면 400 BAD REQUEST를 반환한다.")
    @Test
    void createStationWithDuplicateName() {
        GANGNAM.requestSave();

        ExtractableResponse<Response> response = GANGNAM.requestSave();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).isEqualTo("이미 존재하는 역 이름입니다.");
    }

    @DisplayName("전체 역을 조회를 요청하면 등록된 강남역과 역삼역 정보를 조회하고 200 OK를 반환한다.")
    @Test
    void getStations() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();

        ExtractableResponse<Response> response = get("/stations");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> resultLineIds = response.jsonPath()
                .getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsOnly(gangnam.getId(), yeoksam.getId());
    }

    @DisplayName("등록되어있는 역의 id를 URI에 담아서 삭제 요청을 하면 204 NO CONTENT를 반환한다.")
    @Test
    void deleteStation() {
        String uri = GANGNAM.requestSave()
                .header("Location");

        ExtractableResponse<Response> response = delete(uri);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("해당 역을 포함하고 있는 구간이 있을 경우, 삭제 요청을 하면 400 BAD REQUEST를 반환한다.")
    @Test
    void notAllowDeleteStation() {
        Station gangnam = GANGNAM.save();
        Station yeoksam = YEOKSAM.save();
        LINE_2.requestSave(gangnam, yeoksam, 1);

        ExtractableResponse<Response> response = delete("/stations/" + gangnam.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 역 이름으로 null 또는 공백을 넣어서 요청하면 400 BAD REQUEST를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void notAllowNullOrBlankName(String name) {
        StationRequest stationRequest = new StationRequest(name);

        ExtractableResponse<Response> response = post(stationRequest, "/stations");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().asString()).contains("빈 값일 수 없습니다.");
    }

}
