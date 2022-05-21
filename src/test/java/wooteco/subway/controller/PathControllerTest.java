package wooteco.subway.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class PathControllerTest extends ControllerTest {

    @Test
    void showPath() {
        var stationResponse1 = requestCreate("/stations", Map.of("name", "테스트1역"));
        var stationResponse2 = requestCreate("/stations", Map.of("name", "테스트2역"));
        var stationResponse3 = requestCreate("/stations", Map.of("name", "테스트3역"));

        var lineResponse = requestCreate("/lines", Map.of(
                        "name", "테스트1호선",
                        "color", "테스트색상",
                        "upStationId", getId(stationResponse1),
                        "downStationId", getId(stationResponse2),
                        "distance", "2"
                )
        );

        requestCreate("/lines/" + getId(lineResponse) + "/sections", Map.of(
                        "upStationId", getId(stationResponse2),
                        "downStationId", getId(stationResponse3),
                        "distance", "2"
                )
        );

        var pathResponse = requestGet(
                "/paths?source=" + getId(stationResponse1) + "&target=" + getId(stationResponse3) + "&age=0"
        );

        assertAll(
                () -> assertThat(pathResponse.body().jsonPath().getList("stations").size()).isEqualTo(3),
                () -> assertThat(pathResponse.body().jsonPath().getInt("distance")).isEqualTo(4),
                () -> assertThat(pathResponse.body().jsonPath().getInt("fare")).isEqualTo(1250)
        );
    }
}
