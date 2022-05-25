package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.LineServiceResponse;
import wooteco.subway.service.dto.SectionServiceRequest;

@SpringBootTest
class SectionServiceTest extends ServiceTest {

    private static final Station 잠실역 = new Station("잠실역");
    private static final Station 성수역 = new Station("성수역");

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineService lineService;
    private final SectionService sectionService;

    private Station upStation;
    private Station downStation;
    private LineServiceResponse lineServiceResponse;

    @Autowired
    public SectionServiceTest(StationDao stationDao, SectionDao sectionDao, LineService lineService,
                              SectionService sectionService) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @BeforeEach
    void setUp() {
        this.upStation = stationDao.save(강남역);
        this.downStation = stationDao.save(선릉역);
        this.lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                upStation.getId(),
                downStation.getId(),
                10,
                0));

    }

    @DisplayName("상행 종점이 같은 구간을 저장한다.")
    @Test
    void saveSameUpStation() {
        final Station middleStation = stationDao.save(잠실역);

        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                upStation.getId(),
                middleStation.getId(),
                5);

        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("하행 종점이 같은 구간을 저장한다.")
    @Test
    void saveSameDownStation() {
        final Station middleStation = stationDao.save(잠실역);

        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                middleStation.getId(),
                downStation.getId(),
                5);

        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("상행 종점과 하행 종점이 같은 구간을 저장한다.")
    @Test
    void saveExtendUp() {
        final Station farUpStation = stationDao.save(잠실역);

        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                farUpStation.getId(),
                upStation.getId(),
                10);

        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("하행 종점과 상행 종점이 같은 구간을 저장한다.")
    @Test
    void saveExtendDown() {
        final Station farDownStation = stationDao.save(잠실역);

        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                downStation.getId(),
                farDownStation.getId(),
                10);

        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("상행과 하행 종점이 모두 같은 구간을 저장할 경우 예외가 발생한다.")
    @Test
    void saveSameEndStations() {
        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                upStation.getId(),
                downStation.getId(),
                5);

        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행과 하행 종점 모두 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void saveNotExistingSection() {
        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                upStation.getId(),
                downStation.getId(),
                10);

        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행 종점을 제거한다.")
    @Test
    void deleteUpStation() {
        final Station farDownStation = stationDao.save(잠실역);

        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                downStation.getId(),
                farDownStation.getId(),
                10);

        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), upStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("하행 종점을 제거한다.")
    @Test
    void deleteDownStation() {
        final Station farUpStation = stationDao.save(잠실역);

        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                farUpStation.getId(),
                upStation.getId(),
                10);

        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), downStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("중간역을 제거한다.")
    @Test
    void deleteMiddleStation() {
        final Station farUpStation = stationDao.save(잠실역);

        final SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                farUpStation.getId(),
                upStation.getId(),
                10);

        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), upStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("구간이 하나뿐인 노선에서 구간을 제거할 경우 예외가 발생한다.")
    @Test
    void deleteOnlySection() {
        assertThatThrownBy(() -> sectionService.delete(lineServiceResponse.getId(), downStation.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("노선에 존재하지 않는 역을 제거할 경우 예외가 발생한다.")
    @Test
    void deleteNotExistingStation() {
        final Station farDownStation = stationDao.save(잠실역);

        final Station unregistered = stationDao.save(성수역);

        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(),
                downStation.getId(),
                farDownStation.getId(),
                10);

        sectionService.connect(sectionServiceRequest);

        assertThatThrownBy(() -> sectionService.delete(lineServiceResponse.getId(), unregistered.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
