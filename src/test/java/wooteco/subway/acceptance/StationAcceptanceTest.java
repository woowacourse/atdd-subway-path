package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import wooteco.subway.acceptance.fixture.SubwayFixture;
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
        SimpleResponse response = SubwayFixture.createStation(new StationRequest("강남역"));
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
        SubwayFixture.createStation(new StationRequest("강남역"));
        // when
        SimpleResponse response = SubwayFixture.createStation(new StationRequest("강남역"));
        // then
        response.assertStatus(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        SimpleResponse 강남역 = SubwayFixture.createStation(new StationRequest("강남역"));
        SimpleResponse 역삼역 = SubwayFixture.createStation(new StationRequest("역삼역"));

        // when
        SimpleResponse response = SimpleRestAssured.get("/stations");

        // then
        List<Long> expectedLineIds = Stream.of(강남역, 역삼역)
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
        SimpleResponse 강남역 = SubwayFixture.createStation(new StationRequest("강남역"));
        // when
        String uri = 강남역.getHeader("Location");
        SimpleResponse response = SimpleRestAssured.delete(uri);
        // then
        response.assertStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("존재하지 않는 지하철 역을 삭제하면 예외를 던진다.")
    void deleteStation_throwsExceptionWithInvalidStation() {
        // given
        SubwayFixture.createStation(new StationRequest("강남역"));
        // when
        SimpleResponse deleteResponse = SimpleRestAssured.delete("/lines/100");
        // then
        deleteResponse.assertStatus(HttpStatus.BAD_REQUEST);
    }
}
