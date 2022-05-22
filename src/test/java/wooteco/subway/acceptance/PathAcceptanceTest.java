package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.StationEntity;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.LineResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineService lineService;

    private StationEntity gangnam;
    private StationEntity nowon;
    private StationEntity jamsil;

    private LineResponse line1;
    private LineResponse line2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        gangnam = stationDao.save(new StationEntity(null, "강남"));
        nowon = stationDao.save(new StationEntity(null, "노원"));
        jamsil = stationDao.save(new StationEntity(null, "잠실"));
        line1 = lineService.save(new LineRequest("1호선", "red", gangnam.getId(), nowon.getId(), 46));
        line2 = lineService.save(new LineRequest("2호선", "green", nowon.getId(), jamsil.getId(), 2));
        line2 = lineService.save(new LineRequest("3호선", "grey", gangnam.getId(), jamsil.getId(), 100));
    }

    @Test
    @DisplayName("경로 조회하기")
    void getPath() {
        // given
        String url = "/paths?source=" + gangnam.getId() + "&target=" + jamsil.getId() + "&age=15";
        ExtractableResponse<Response> response = get(url);
        // when
        PathResponse pathResponse = response.body().as(PathResponse.class);
        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations().size()).isEqualTo(3);
            assertThat(pathResponse.getDistance()).isEqualTo(48);
            assertThat(pathResponse.getFare()).isEqualTo(2050L);
        });
    }
}
