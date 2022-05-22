package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.helper.TStation.DONGMYO;
import static wooteco.subway.helper.TStation.SINDANG;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        ExtractableResponse<Response> response = SINDANG.역을등록한다(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotNull();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        SINDANG.역을등록한다();
        SINDANG.역을등록한다(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        SINDANG.역을등록한다();
        List<StationResponse> response = DONGMYO.역을등록하고().역을조회한다(HttpStatus.OK.value());
        assertThat(response).containsExactly(SINDANG.역(), DONGMYO.역());
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        SINDANG.역을등록하고()
                .역을제거한다(HttpStatus.NO_CONTENT.value());
    }
}
