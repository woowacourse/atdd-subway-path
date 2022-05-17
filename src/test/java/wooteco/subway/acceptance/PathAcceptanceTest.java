package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.service.dto.StationResponse;
import wooteco.subway.utils.RestAssuredUtil;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로를 조회한다.")
    @Test
    void searchPath() {
        //given
        Long sourceId = 1L;
        Long targetId = 2L;
        String url = "/paths?source=" + sourceId + "&target=" + targetId + "&age=15";

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.get(url);

        //then
        Integer fare = response.jsonPath().get("fare");
        Integer distance = response.jsonPath().get("distance");
        List<Long> stationIds = generateIds(response.jsonPath().getList("stations", StationResponse.class));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(fare).isEqualTo(1250),
                () -> assertThat(distance).isEqualTo(5),
                () -> assertThat(stationIds).contains(sourceId, targetId)
        );
    }

    private List<Long> generateIds(List<StationResponse> stations) {
        return stations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
