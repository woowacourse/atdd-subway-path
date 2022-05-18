package wooteco.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.response.ExceptionResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.RowNotFoundException;
import wooteco.subway.exception.SectionNotEnoughException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("구간을 추가한다.")
    void enrollSection() {
        //given
        final String uri = setLineAsSaved().header("Location") + "/sections";
        SimpleRestAssured.post("/stations", Map.of("name", "C역"));

        Map<String, String> params = Map.of(
                "upStationId", "2",
                "downStationId", "3",
                "distance", "5"
        );

        //when
        final ExtractableResponse<Response> response = SimpleRestAssured.post(uri, params);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    @DisplayName("존재하지 않는 상행역, 하행역을 입력한 경우 추가 수 없다.")
    void enrollSection_throwsExceptionWithNotExistStation() {
        //given
        final String uri = setLineAsSaved().header("Location") + "/sections";
        Map<String, String> params = Map.of(
                "upStationId", "3",
                "downStationId", "4",
                "distance", "5"
        );

        //when
        final ExtractableResponse<Response> response = SimpleRestAssured.post(uri, params);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1"})
    @DisplayName("양의 정수가 아닌 거리를 입력한 경우 추가할 수 없다.")
    void enrollSection_throwsExceptionWithWrongDistance(String distance) {
        //given
        final String uri = setLineAsSaved().header("Location") + "/sections";
        SimpleRestAssured.post("/stations", Map.of("name", "C역"));
        Map<String, String> params = Map.of(
                "upStationId", "2",
                "downStationId", "3",
                "distance", distance
        );

        //when
        final ExtractableResponse<Response> response = SimpleRestAssured.post(uri, params);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("구간을 삭제할 수 있다.")
    void deleteSection() {
        //given
        final String uri = setLineAsSaved().header("Location") + "/sections";
        SimpleRestAssured.post("/stations", Map.of("name", "C역"));
        SimpleRestAssured.post(uri, Map.of(
                "upStationId", "2",
                "downStationId", "3",
                "distance", "5"));

        //when
        final ExtractableResponse<Response> response = SimpleRestAssured.delete(uri + "?stationId=2");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("존재하지 않는 지하철역은 삭제할 수 없다.")
    void deleteSection_throwExceptionWithInvalidId() {
        //given
        final String uri = setLineAsSaved().header("Location") + "/sections";
        SimpleRestAssured.post("/stations", Map.of("name", "C역"));
        SimpleRestAssured.post(uri, Map.of(
                "upStationId", "2",
                "downStationId", "3",
                "distance", "5"));

        //when
        final ExtractableResponse<Response> response = SimpleRestAssured.delete(uri + "?stationId=5");

        //then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.toObject(response, ExceptionResponse.class).getException())
                        .isEqualTo(RowNotFoundException.class)
        );
    }

    @Test
    @DisplayName("구간이 하나 남은 경우 삭제할 수 없다.")
    void deleteSection_throwExceptionWithOneSection() {
        //given
        final String uri = setLineAsSaved().header("Location") + "/sections";

        //when
        final ExtractableResponse<Response> response = SimpleRestAssured.delete(uri + "?stationId=1");

        //then
        Assertions.assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(SimpleRestAssured.toObject(response, ExceptionResponse.class).getException())
                        .isEqualTo(SectionNotEnoughException.class)
        );
    }


    private ExtractableResponse<Response> setLineAsSaved() {
        final Long upStationId = SimpleRestAssured.toObject(
                SimpleRestAssured.post("/stations", Map.of("name", "A역")), StationResponse.class).getId();

        final Long downStationId = SimpleRestAssured.toObject(
                SimpleRestAssured.post("/stations", Map.of("name", "B역")), StationResponse.class).getId();

        Map<String, String> params = Map.of(
                "name", "a호선",
                "color", "bg-red-600",
                "upStationId", upStationId + "",
                "downStationId", downStationId + "",
                "distance", "10");

        return SimpleRestAssured.post("/lines", params);
    }

}
