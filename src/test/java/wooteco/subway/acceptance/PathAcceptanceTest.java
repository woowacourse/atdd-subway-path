package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.acceptance.fixture.SimpleResponse;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.response.PathResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("출발역과 도착역으로 최단 경로를 조회한다.")
    public void getPath() {
        // given
        Map<String, String> stationParams1 = Map.of("name", "강남역");
        Map<String, String> stationParams2 = Map.of("name", "역삼역");
        Map<String, String> stationParams3 = Map.of("name", "선릉역");
        SimpleRestAssured.post("/stations", stationParams1);
        SimpleRestAssured.post("/stations", stationParams2);
        SimpleRestAssured.post("/stations", stationParams3);

        Map<String, String> lineParams = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", "1",
                "downStationId", "2",
                "distance", "10"
        );
        SimpleRestAssured.post("/lines", lineParams);

        Map<String, String> sectionParams =
                Map.of("upStationId", "2",
                        "downStationId", "3",
                        "distance", "5");
        SimpleRestAssured.post("/lines/1/sections", sectionParams);

        Map<String, Integer> params = Map.of("source", 1, "target", 3, "age", 25);

        //when
        SimpleResponse response = SimpleRestAssured.get("/paths?source=1&target=3&age=25");
        PathResponse pathResponse = response.toObject(PathResponse.class);

        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(0.015),
                () -> assertThat(pathResponse.getStationResponses()).hasSize(3),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    @Test
    @DisplayName("이용 요금에 노선의 초과 요금을 합산한다.")
    public void getPath_with_extra_200() {
        // given
        Map<String, String> stationParams1 = Map.of("name", "강남역");
        Map<String, String> stationParams2 = Map.of("name", "역삼역");
        Map<String, String> stationParams3 = Map.of("name", "선릉역");
        SimpleRestAssured.post("/stations", stationParams1);
        SimpleRestAssured.post("/stations", stationParams2);
        SimpleRestAssured.post("/stations", stationParams3);

        Map<String, String> lineParams = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", "1",
                "downStationId", "2",
                "distance", "10",
                "extraFare", "300"
        );
        SimpleRestAssured.post("/lines", lineParams);

        Map<String, String> sectionParams =
                Map.of("upStationId", "2",
                        "downStationId", "3",
                        "distance", "5");
        SimpleRestAssured.post("/lines/1/sections", sectionParams);

        //when
        SimpleResponse response = SimpleRestAssured.get("/paths?source=1&target=3&age=25");
        PathResponse pathResponse = response.toObject(PathResponse.class);

        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(0.015),
                () -> assertThat(pathResponse.getStationResponses()).hasSize(3),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1550)
        );
    }

    @Test
    @DisplayName("13세 청소년이 35km를 이용한 요금은 1120원이다.")
    public void getPath_teenager_35km() {
        // given
        Map<String, String> stationParams1 = Map.of("name", "강남역");
        Map<String, String> stationParams2 = Map.of("name", "역삼역");
        SimpleRestAssured.post("/stations", stationParams1);
        SimpleRestAssured.post("/stations", stationParams2);

        Map<String, String> lineParams = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", "1",
                "downStationId", "2",
                "distance", "35000"
        );
        SimpleRestAssured.post("/lines", lineParams);

        //when
        SimpleResponse response = SimpleRestAssured.get("/paths?source=1&target=2&age=13");
        PathResponse pathResponse = response.toObject(PathResponse.class);

        //then
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(35),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1120)
        );
    }
}
