package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import wooteco.subway.service.LineService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.StationResponse;
import wooteco.subway.ui.dto.LineCreateRequest;
import wooteco.subway.ui.dto.StationRequest;
import wooteco.subway.utils.RestAssuredUtil;

@DisplayName("지하철 경로 관련 기능 - PathAcceptanceTest")
public class PathAcceptanceTest extends AcceptanceTest {

    private Long sourceId;
    private Long targetId;

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @BeforeEach
    void init() {
        sourceId = stationService.save(new StationRequest("강남역")).getId();
        targetId = stationService.save(new StationRequest("왕십리역")).getId();

        lineService.save(new LineCreateRequest("신분당선", "bg-red-600", sourceId, targetId, 10, 500));
    }

    @DisplayName("경로를 조회한다.")
    @Test
    void searchPath() {
        //given
        String url = "/paths?source=" + sourceId + "&target=" + targetId + "&age=20";

        //when
        ExtractableResponse<Response> response = RestAssuredUtil.get(url);

        //then
        Integer fare = response.jsonPath().get("fare");
        Integer distance = response.jsonPath().get("distance");
        List<Long> stationIds = generateIds(response.jsonPath().getList("stations", StationResponse.class));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(fare).isEqualTo(1750),
                () -> assertThat(distance).isEqualTo(10),
                () -> assertThat(stationIds).contains(sourceId, targetId)
        );
    }

    private List<Long> generateIds(List<StationResponse> stations) {
        return stations.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
