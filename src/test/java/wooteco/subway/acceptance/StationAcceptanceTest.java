package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import wooteco.subway.acceptance.fixture.SimpleResponse;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        StationRequest stationRequest = new StationRequest("강남역");
        // when
        SimpleResponse response = SimpleRestAssured.post("/stations", stationRequest);
        // then
        Assertions.assertAll(
                () -> response.assertStatus(HttpStatus.CREATED),
                () -> assertThat(response.getHeader("Location")).isNotBlank()
        );
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        StationRequest stationRequest = new StationRequest("강남역");
        SimpleRestAssured.post("/stations", stationRequest);
        // when
        SimpleResponse response = SimpleRestAssured.post("/stations", stationRequest);
        // then
        response.assertStatus(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        StationRequest 강남역 = new StationRequest("강남역");
        StationRequest 역삼역 = new StationRequest("역삼역");
        SimpleResponse 강남역_생성 = SimpleRestAssured.post("/stations", 강남역);
        SimpleResponse 역삼역_생성 = SimpleRestAssured.post("/stations", 역삼역);

        // when
        SimpleResponse response = SimpleRestAssured.get("/stations");

        // then
        List<Long> expectedLineIds = Stream.of(강남역_생성, 역삼역_생성)
                .map(SimpleResponse::getIdFromLocation)
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.toList(StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        Assertions.assertAll(
                () -> response.assertStatus(HttpStatus.OK),
                () -> assertThat(resultLineIds).containsAll(expectedLineIds)
        );
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationRequest stationRequest = new StationRequest("강남역");
        SimpleResponse createResponse = SimpleRestAssured.post("/stations", stationRequest);
        // when
        String uri = createResponse.getHeader("Location");
        SimpleResponse response = SimpleRestAssured.delete(uri);
        // then
        response.assertStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("존재하지 않는 지하철 역을 삭제하면 예외를 던진다.")
    void deleteStation_throwsExceptionWithInvalidStation() {
        // given
        StationRequest stationRequest = new StationRequest("강남역");
        SimpleRestAssured.post("/stations", stationRequest);
        // when
        SimpleResponse deleteResponse = SimpleRestAssured.delete("/lines/100");
        // then
        deleteResponse.assertStatus(HttpStatus.BAD_REQUEST);
    }
}
