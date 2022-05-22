package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import wooteco.subway.acceptance.DBTest;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.LineServiceResponse;
import wooteco.subway.service.dto.SectionServiceRequest;

@SpringBootTest
class SectionServiceTest extends DBTest {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineService lineService;
    private final SectionService sectionService;

    private Station firstStation;
    private Station secondStation;
    private Station thirdStation;
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
        firstStation = stationDao.save(new Station("강남역"));
        secondStation = stationDao.save(new Station("잠실역"));
        thirdStation = stationDao.save(new Station("선릉역"));
        lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선", "green", firstStation.getId(), secondStation.getId(), 10, 200));
    }

    @DisplayName("상행 종점이 같은 구간을 저장한다.")
    @Test
    void saveSameUpStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), firstStation.getId(), thirdStation.getId(), 5);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("하행 종점이 같은 구간을 저장한다.")
    @Test
    void saveSameDownStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                thirdStation.getId(), secondStation
                .getId(), 5);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("상행 종점과 하행 종점이 같은 구간을 저장한다.")
    @Test
    void saveExtendUp() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), thirdStation.getId(), firstStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("하행 종점과 상행 종점이 같은 구간을 저장한다.")
    @Test
    void saveExtendDown() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                secondStation
                        .getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("상행과 하행 종점이 모두 같은 구간을 저장할 경우 예외가 발생한다.")
    @Test
    void saveSameEndStations() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                firstStation
                        .getId(), secondStation.getId(), 5);
        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행과 하행 종점 모두 존재할 예외가 발생한다.")
    @Test
    void saveNotExistingSection() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                firstStation.getId(), secondStation.getId(), 10);
        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행 종점을 제거한다.")
    @Test
    void deleteUpStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                secondStation.getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), firstStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("하행 종점을 제거한다.")
    @Test
    void deleteDownStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), secondStation.getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), thirdStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("중간역을 제거한다.")
    @Test
    void deleteMiddleStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), secondStation.getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), secondStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("구간이 하나뿐인 노선에서 구간을 제거할 경우 예외가 발생한다.")
    @Test
    void deleteOnlySection() {

        assertThatThrownBy(() -> sectionService.delete(lineServiceResponse.getId(), secondStation.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("노선에 존재하지 않는 역을 제거할 경우 예외가 발생한다.")
    @Test
    void deleteNotExistingStation() {
        Station fourthStation = stationDao.save(new Station("성수역"));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), secondStation.getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThatThrownBy(() -> sectionService.delete(lineServiceResponse.getId(), fourthStation.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
