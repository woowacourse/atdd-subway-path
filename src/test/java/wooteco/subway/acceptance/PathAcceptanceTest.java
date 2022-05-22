package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("5개의 역을 생성한다 -> 2개의 노선을 생성한다 -> 구간을 추가한다 -> 경로를 검색한다")
    void showPath() {
        //given
        var stationResponse1 = requestCreate("/stations", Map.of("name", "테스트1역"));
        var stationResponse2 = requestCreate("/stations", Map.of("name", "테스트2역"));
        var stationResponse3 = requestCreate("/stations", Map.of("name", "테스트3역"));
        var stationResponse4 = requestCreate("/stations", Map.of("name", "테스트4역"));
        var stationResponse5 = requestCreate("/stations", Map.of("name", "테스트5역"));

        var lineResponse1 = requestCreate("/lines", Map.of(
                        "name", "테스트1호선",
                        "color", "테스트1색상",
                        "upStationId", getId(stationResponse1),
                        "downStationId", getId(stationResponse2),
                        "distance", "4"
                )
        );

        var lineResponse2 = requestCreate("/lines", Map.of(
                        "name", "테스트2호선",
                        "color", "테스트2색상",
                        "upStationId", getId(stationResponse1),
                        "downStationId", getId(stationResponse2),
                        "distance", "4"
                )
        );

        requestCreate("/lines/" + getId(lineResponse1) + "/sections", Map.of(
                        "upStationId", getId(stationResponse2),
                        "downStationId", getId(stationResponse3),
                        "distance", "100"
                )
        );

        requestCreate("/lines/" + getId(lineResponse2) + "/sections", Map.of(
                        "upStationId", getId(stationResponse2),
                        "downStationId", getId(stationResponse4),
                        "distance", "4"
                )
        );

        requestCreate("/lines/" + getId(lineResponse2) + "/sections", Map.of(
                        "upStationId", getId(stationResponse4),
                        "downStationId", getId(stationResponse5),
                        "distance", "4"
                )
        );

        requestCreate("/lines/" + getId(lineResponse2) + "/sections", Map.of(
                        "upStationId", getId(stationResponse5),
                        "downStationId", getId(stationResponse3),
                        "distance", "4"
                )
        );

        //when
        var pathResponse = requestGet(
                "/paths?source=" + getId(stationResponse1) + "&target=" + getId(stationResponse3) + "&age=0"
        );

        //then
        assertAll(
                () -> assertThat(pathResponse.body().jsonPath().getList("stations").size()).isEqualTo(5),
                () -> assertThat(pathResponse.body().jsonPath().getInt("distance")).isEqualTo(16),
                () -> assertThat(pathResponse.body().jsonPath().getInt("fare")).isEqualTo(1450)
        );
    }
}
