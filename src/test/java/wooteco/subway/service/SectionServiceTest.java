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
import wooteco.subway.service.dto.LineCreationServiceRequest;
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
        lineServiceResponse = lineService.save(new LineCreationServiceRequest(
                "2호선", "green", firstStation.getId(), secondStation.getId(), 10, 200));
    }

    @DisplayName("상행 종점이 같은 구간을 연결한다.")
    @Test
    void save_sameUpStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), firstStation.getId(), thirdStation.getId(), 5);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("하행 종점이 같은 구간을 연결한다.")
    @Test
    void save_sameDownStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                thirdStation.getId(), secondStation
                .getId(), 5);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("상행 종점과 하행 종점이 같은 구간을 연결한다.")
    @Test
    void connect_extendingForward() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), thirdStation.getId(), firstStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("하행 종점과 상행 종점이 같은 구간을 연결한다.")
    @Test
    void connect_extendingBackward() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                secondStation
                        .getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("역간의 거리가 1 미만인 구간을 연결하려고 하면 예외를 발생시킨다.")
    @Test
    void connect_exception_distanceLowerThanOne() {
        Integer invalidDistance = 0;
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), firstStation.getId(), thirdStation.getId(), invalidDistance);

        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역간의 거리는 1 이상이어야 합니다.");
    }

    @DisplayName("상행과 하행 종점이 모두 같은 구간을 연결하려고 하면 예외가 발생시킨다.")
    @Test
    void connect_exception_SameUpStationAndSameDownStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                firstStation
                        .getId(), secondStation.getId(), 5);
        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행역과 하행역 모두 존재하는 구간을 연결하려고 하면 예외를 발생시킨다.")
    @Test
    void connect_exception_bothExistingStations() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                firstStation.getId(), secondStation.getId(), 10);
        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행 종점에 해당하는 구간을 제거한다.")
    @Test
    void delete_upStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(),
                secondStation.getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), firstStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("하행 종점에 해당 하는 구간을 제거한다.")
    @Test
    void delete_downStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), secondStation.getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), thirdStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("중간역에 해당하는 구간을 제거한다.")
    @Test
    void delete_middleStation() {
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), secondStation.getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), secondStation.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("구간이 하나뿐인 노선에서 구간을 제거할 경우 예외가 발생한다.")
    @Test
    void delete_exception_onlySection() {

        assertThatThrownBy(() -> sectionService.delete(lineServiceResponse.getId(), secondStation.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("노선에 존재하지 않는 역을 제거할 경우 예외가 발생한다.")
    @Test
    void delete_exception_notExisting() {
        Station fourthStation = stationDao.save(new Station("성수역"));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(
                lineServiceResponse.getId(), secondStation.getId(), thirdStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThatThrownBy(() -> sectionService.delete(lineServiceResponse.getId(), fourthStation.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
