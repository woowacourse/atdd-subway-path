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
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationResponse;
import wooteco.subway.ui.dto.LineCreateRequest;
import wooteco.subway.utils.RestAssuredUtil;

@DisplayName("지하철 경로 관련 기능 - PathAcceptanceTest")
public class PathAcceptanceTest extends AcceptanceTest {

    private Long sourceId;
    private Long targetId;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @BeforeEach
    void init() {
        sourceId = stationDao.save(new Station("강남역"));
        targetId = stationDao.save(new Station("왕십리역"));

        LineCreateRequest lineCreateRequest = new LineCreateRequest("3호선", "red", sourceId, targetId, 10, 500);
        Long lineId = lineDao.save(lineCreateRequest);

        sectionDao.save(new Section(lineId, sourceId, targetId, 10));
    }

    @DisplayName("경로를 조회한다.")
    @Test
    void searchPath() {
        //given
        String url = "/paths?source=" + sourceId + "&target=" + targetId + "&age=15";

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
