package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.LineEntity;
import wooteco.subway.repository.entity.SectionEntity;
import wooteco.subway.repository.entity.StationEntity;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    StationEntity 동묘앞역;
    StationEntity 신설동역;
    StationEntity 용두역;
    StationEntity 신답역;
    StationEntity 용답역;
    StationEntity 성수역;
    StationEntity 건대입구역;

    LineEntity 일호선;
    LineEntity 이호선;
    LineEntity 삼호선;

    SectionEntity 일호선구간_1;
    SectionEntity 일호선구간_2;
    SectionEntity 일호선구간_3;
    SectionEntity 일호선구간_4;
    SectionEntity 일호선구간_5;
    SectionEntity 이호선구간_1;
    SectionEntity 삼호선구간_1;

    @BeforeEach
    void setUp() {
        동묘앞역 = stationDao.save(new StationEntity(null, "동묘앞역"));
        신설동역 = stationDao.save(new StationEntity(null, "신설동역"));
        용두역 = stationDao.save(new StationEntity(null, "용두역"));
        신답역 = stationDao.save(new StationEntity(null, "신답역"));
        용답역 = stationDao.save(new StationEntity(null, "용답역"));
        성수역 = stationDao.save(new StationEntity(null, "성수역"));
        건대입구역 = stationDao.save(new StationEntity(null, "건대입구역"));

        일호선 = lineDao.save(new LineEntity(null, "1호선", "blue", 0));
        이호선 = lineDao.save(new LineEntity(null, "2호선", "green", 900));
        삼호선 = lineDao.save(new LineEntity(null, "3호선", "orange", 500));

        일호선구간_1 = sectionDao.save(
                new SectionEntity(null, 일호선.getId(), 동묘앞역.getId(), 신설동역.getId(), 20));
        일호선구간_2 = sectionDao.save(
                new SectionEntity(null, 일호선.getId(), 신설동역.getId(), 용두역.getId(), 5));
        일호선구간_3 = sectionDao.save(
                new SectionEntity(null, 일호선.getId(), 용두역.getId(), 신답역.getId(), 30));
        일호선구간_4 = sectionDao.save(
                new SectionEntity(null, 일호선.getId(), 신답역.getId(), 용답역.getId(), 30));
        일호선구간_5 = sectionDao.save(
                new SectionEntity(null, 일호선.getId(), 용답역.getId(), 성수역.getId(), 30));
        이호선구간_1 = sectionDao.save(
                new SectionEntity(null, 이호선.getId(), 성수역.getId(), 건대입구역.getId(), 12));
        삼호선구간_1 = sectionDao.save(
                new SectionEntity(null, 삼호선.getId(), 신답역.getId(), 성수역.getId(), 20));
    }

    @ParameterizedTest
    @CsvSource(value = {"21,1250", "13,720", "6,450", "3,0"})
    @DisplayName("경로 조회 시 10Km 미만일 경우 기본 요금이 부과된다.")
    void getPath_defaultFare(int age, int fare) throws Exception {
        // given
        PathResponse pathResponse = pathService.getPath(신설동역.getId(), 용두역.getId(), age);

        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations()).hasSize(2);
            assertThat(pathResponse.getDistance()).isEqualTo(5);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"21,1750", "13,1120", "6,700", "3,0"})
    @DisplayName("경로 조회 시 10Km 이상 50Km 미만일 경우 10km를 제외한 거리에서 5km당 100원 씩 추가된 요금이 부과된다.")
    void getPath_surchargeFare(int age, int fare) throws Exception {
        // given
        PathResponse pathResponse = pathService.getPath(신설동역.getId(), 신답역.getId(), age);

        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations()).hasSize(3);
            assertThat(pathResponse.getDistance()).isEqualTo(35);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"21,2150", "13,1440", "6,900", "3,0"})
    @DisplayName("경로 조회 시 50Km 이상일 경우 50km를 제외한 거리에서 8km당 100원 씩 추가된 요금이 부과된다.")
    void getPath_extraSurchargeFare(int age, int fare) throws Exception {
        // given
        PathResponse pathResponse = pathService.getPath(동묘앞역.getId(), 신답역.getId(), age);

        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations()).hasSize(4);
            assertThat(pathResponse.getDistance()).isEqualTo(55);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"21,2650", "13,1840", "6,1150", "3,0"})
    @DisplayName("경로 조회 시 추가 요금이 있는 노선을 경유한 경우 계산된 요금에 추가 요금을 더한 금액이 부과된다.")
    void getPath_extraFareLine(int age, int fare) throws Exception {
        // given
        PathResponse pathResponse = pathService.getPath(신설동역.getId(), 성수역.getId(), age);

        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations()).hasSize(4);
            assertThat(pathResponse.getDistance()).isEqualTo(55);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }

    @ParameterizedTest
    @CsvSource(value = {"21,3250", "13,2320", "6,1450", "3,0"})
    @DisplayName("경로 조회 시 추가 요금이 있는 노선을 여러 개  경유한 경우 계산된 요금에 가장 높은 추가 요금을 더한 금액이 부과된다.")
    void getPath_maxExtraFareLine(int age, int fare) throws Exception {
        // given
        PathResponse pathResponse = pathService.getPath(신설동역.getId(), 건대입구역.getId(), age);

        // then
        assertAll(() -> {
            assertThat(pathResponse.getStations()).hasSize(5);
            assertThat(pathResponse.getDistance()).isEqualTo(67);
            assertThat(pathResponse.getFare()).isEqualTo(fare);
        });
    }
}
