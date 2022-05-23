package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.SectionEntity;
import wooteco.subway.repository.entity.StationEntity;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.LineResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineService lineService;

    @Autowired
    private SectionService sectionService;

    private StationEntity 신설동역;
    private StationEntity 용두역;
    private StationEntity 신답역;
    private StationEntity 성수역;
    private StationEntity 건대입구역;

    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        신설동역 = stationDao.save(new StationEntity(null, "신설동역"));
        용두역 = stationDao.save(new StationEntity(null, "용두역"));
        신답역 = stationDao.save(new StationEntity(null, "신답역"));
        성수역 = stationDao.save(new StationEntity(null, "성수역"));
        건대입구역 = stationDao.save(new StationEntity(null, "건대입구역"));

        이호선 = lineService.save(new LineRequest("2호선", "green", 신설동역.getId(), 성수역.getId(), 42, 0));
        sectionDao.save(new SectionEntity(null, 이호선.getId(), 신설동역.getId(), 용두역.getId(), 10));
        sectionDao.save(new SectionEntity(null, 이호선.getId(), 용두역.getId(), 신답역.getId(), 20));
        sectionDao.save(new SectionEntity(null, 이호선.getId(), 신답역.getId(), 성수역.getId(), 12));

        일호선 = lineService.save(new LineRequest("1호선", "red", 용두역.getId(), 성수역.getId(), 17, 800));

        삼호선 = lineService.save(new LineRequest("3호선", "orange", 성수역.getId(), 건대입구역.getId(), 10, 900));
    }

    @ParameterizedTest
    @CsvSource(value = {"21,1650", "13,1040", "6,650", "3,0"})
    @DisplayName("선택한 출발지와 목적지에 해당하는 경로를 조회한다.")
    void getPath(int age, int fare) {
        // given
        String url = "/paths?source=" + 신설동역.getId() + "&target=" + 신답역.getId() + "&age=" + age;
        ExtractableResponse<Response> response = get(url);
        // when
        PathResponse pathResponse = response.body().as(PathResponse.class);
        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations().size()).isEqualTo(3);
            assertThat(pathResponse.getDistance()).isEqualTo(30);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"21,2450", "13,1680", "6,1050", "3,0"})
    @DisplayName("추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가한다.")
    void getPath_WhenIncludeExtraFareLine(int age, int fare) {
        // given
        String url = "/paths?source=" + 신설동역.getId() + "&target=" + 성수역.getId() + "&age=" + age;
        ExtractableResponse<Response> response = get(url);
        // when
        PathResponse pathResponse = response.body().as(PathResponse.class);
        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations().size()).isEqualTo(3);
            assertThat(pathResponse.getDistance()).isEqualTo(27);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"21,2750", "13,1920", "6,1200", "3,0"})
    @DisplayName("추가 요금이 있는 노선을 여러 개 이용 할 경우 가장 높은 추가 금액만 적용하여 측정된 요금에 추가한다.")
    void getPath_WhenIncludeSomeExtraFareLine(int age, int fare) {
        // given
        String url = "/paths?source=" + 신설동역.getId() + "&target=" + 건대입구역.getId() + "&age=" + age;
        ExtractableResponse<Response> response = get(url);
        // when
        PathResponse pathResponse = response.body().as(PathResponse.class);
        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations().size()).isEqualTo(4);
            assertThat(pathResponse.getDistance()).isEqualTo(37);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }
}
