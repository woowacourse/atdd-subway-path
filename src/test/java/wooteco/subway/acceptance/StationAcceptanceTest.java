package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given & when
        StationRequest 선릉 = new StationRequest("선릉역");
        ExtractableResponse<Response> response = insert(convertRequest(선릉), "/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성할 시 400에러를 발생한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        insert(convertRequest(선릉), "/stations");

        // when
        ExtractableResponse<Response> response = insert(convertRequest(선릉), "/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        ExtractableResponse<Response> createResponse1 = insert(convertRequest(선릉), "/stations");
        ExtractableResponse<Response> createResponse2 = insert(convertRequest(잠실), "/stations");

        // when
        ExtractableResponse<Response> response = get("/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        ExtractableResponse<Response> createResponse = insert(convertRequest(선릉), "/stations");

        // when
        String path = createResponse.header("Location");
        ExtractableResponse<Response> response = delete(path);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 지하철역을 삭제할 시 404에러를 발생시킨다.")
    @Test
    void deleteStationNotExist() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        ExtractableResponse<Response> createResponse = insert(convertRequest(선릉), "/stations");

        // when
        int id = Integer.parseInt(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = delete("/stations/" + id + 1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    @DisplayName("구간에 추간된 지하철역을 삭제할 시 400에러를 발생시킨다.")
    @Test
    void deleteStationInSection() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        ExtractableResponse<Response> createResponse = insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        // when
        String path = createResponse.header("Location");
        ExtractableResponse<Response> response = delete(path);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
