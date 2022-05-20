package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;

public class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        SimpleRestAssured.post("/stations", Map.of("name", "A역"));
        SimpleRestAssured.post("/stations", Map.of("name", "B역"));
        SimpleRestAssured.post("/stations", Map.of("name", "C역"));

        Map<String, String> lineParams = Map.of(
            "name", "신분당선",
            "color", "bg-red-600",
            "upStationId", "1",
            "downStationId", "2",
            "distance", "10");
        SimpleRestAssured.post("/lines", lineParams);

        Map<String, String> sectionParams = Map.of(
            "upStationId", "2",
            "downStationId", "3",
            "distance", "6"
        );
    }

    @Test
    @DisplayName("구간을 등록한다.")
    public void enrollSection() {
        // given
        Map<String, String> sectionParams = Map.of(
            "upStationId", "2",
            "downStationId", "3",
            "distance", "6"
        );
        // when
        final ExtractableResponse<Response> response = SimpleRestAssured.post("/lines/1/sections", sectionParams);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("잘못된 구간을 등록하면 예외를 던진다.")
    public void throwsExceptionOnInvalidSection() {
        // given
        Map<String, String> sectionParams = Map.of(
            "upStationId", "999",
            "downStationId", "3",
            "distance", "6"
        );
        // when
        final ExtractableResponse<Response> response = SimpleRestAssured.post("/lines/1/sections", sectionParams);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    public void deleteSection() {
        // given
        final ExtractableResponse<Response> response = SimpleRestAssured.delete("/lines/1/sections?stationId=");

        // when

        // then
    }
}
