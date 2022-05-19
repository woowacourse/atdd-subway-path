package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.AcceptanceFixture.낙성대;
import static wooteco.subway.acceptance.AcceptanceFixture.봉천;
import static wooteco.subway.acceptance.AcceptanceFixture.서울대입구;
import static wooteco.subway.acceptance.ResponseCreator.createDeleteStationResponseById;
import static wooteco.subway.acceptance.ResponseCreator.createGetStationResponse;
import static wooteco.subway.acceptance.ResponseCreator.createPostStationResponse;
import static wooteco.subway.acceptance.ResponseCreator.postIds;
import static wooteco.subway.acceptance.ResponseCreator.responseIds;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.controller.dto.station.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        // when
        ExtractableResponse<Response> response = createPostStationResponse(봉천);
        StationResponse stationResponse = response.body().jsonPath().getObject(".", StationResponse.class);
        System.out.println(stationResponse.getId());
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(stationResponse.getName()).isEqualTo(봉천.getName()),
                () -> assertThat(stationResponse.getId()).isNotNull()
        );
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 생성시 예외가 발생한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        // when
        createPostStationResponse(서울대입구);
        ExtractableResponse<Response> response = createPostStationResponse(서울대입구);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> 서울대입구응답 = createPostStationResponse(서울대입구);
        ExtractableResponse<Response> 낙성대응답 = createPostStationResponse(낙성대);
        // when
        ExtractableResponse<Response> response = createGetStationResponse();
        List<StationResponse> stationResponses = response.body().jsonPath().getList(".", StationResponse.class);
        List<Long> 추가한Id = postIds(서울대입구응답, 낙성대응답);
        List<Long> 전체Id = responseIds(response);
        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(전체Id).containsAll(추가한Id),
                () -> assertThat(stationResponses.stream().map(it -> it.getName()).collect(Collectors.toList()))
                        .containsAll(List.of(서울대입구.getName(), 낙성대.getName()))
        );
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = createPostStationResponse(서울대입구);
        String id = createResponse.header("Location").split("/")[2];
        // when
        ExtractableResponse<Response> response = createDeleteStationResponseById(id);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 지하철역을 제거한다.")
    @Test
    void deleteNonStation() {
        // given
        // when
        ExtractableResponse<Response> response = createDeleteStationResponseById("-1");
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
