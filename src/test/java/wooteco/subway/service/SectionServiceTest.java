package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineService lineService;
    private final SectionService sectionService;

    @Autowired
    public SectionServiceTest(StationDao stationDao, SectionDao sectionDao, LineService lineService,
                              SectionService sectionService) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @DisplayName("상행 종점이 같은 구간을 저장한다.")
    @Test
    void saveSameUpStation() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("잠실역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                upStation.getId(),
                downStation.getId(),
                10));

        Station middleStation = stationDao.save(new Station("선릉역"));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), upStation.getId(), middleStation.getId(), 5);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("하행 종점이 같은 구간을 저장한다.")
    @Test
    void saveSameDownStation() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("잠실역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                upStation.getId(),
                downStation.getId(),
                10));

        Station middleStation = stationDao.save(new Station("선릉역"));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), middleStation.getId(), downStation.getId(), 5);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("상행 종점과 하행 종점이 같은 구간을 저장한다.")
    @Test
    void saveExtendUp() {
        Station upStation = stationDao.save(new Station("선릉역"));
        Station downStation = stationDao.save(new Station("잠실역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                upStation.getId(),
                downStation.getId(),
                10));

        Station farUpStation = stationDao.save(new Station("강남역"));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), farUpStation.getId(), upStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("하행 종점과 상행 종점이 같은 구간을 저장한다.")
    @Test
    void saveExtendDown() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                upStation.getId(),
                downStation.getId(),
                10));

        Station farDownStation = stationDao.save(new Station("잠실역"));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), downStation.getId(), farDownStation.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(2);
    }

    @DisplayName("상행과 하행 종점이 모두 같은 구간을 저장할 경우 예외가 발생한다.")
    @Test
    void saveSameEndStations() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("잠실역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                upStation.getId(),
                downStation.getId(),
                10));

        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), upStation.getId(), downStation.getId(), 5);
        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행과 하행 종점 모두 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void saveNotExistingSection() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));
        LineServiceRequest lineServiceRequest = new LineServiceRequest("2호선", "green", upStation.getId(), downStation.getId(), 10);
        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), upStation.getId(), downStation.getId(), 10);
        assertThatThrownBy(() -> sectionService.connect(sectionServiceRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행 종점을 제거한다.")
    @Test
    void deleteUpStation() {
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("선릉역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                station1.getId(),
                station2.getId(),
                10));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), station2.getId(), station3.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), station1.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("하행 종점을 제거한다.")
    @Test
    void deleteDownStation() {
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("선릉역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                station1.getId(),
                station2.getId(),
                10));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), station2.getId(), station3.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), station3.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("중간역을 제거한다.")
    @Test
    void deleteMiddleStation() {
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("선릉역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                station1.getId(),
                station2.getId(),
                10));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), station2.getId(), station3.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        sectionService.delete(lineServiceResponse.getId(), station2.getId());

        assertThat(sectionDao.findAllByLineId(lineServiceResponse.getId()).size()).isEqualTo(1);
    }

    @DisplayName("구간이 하나뿐인 노선에서 구간을 제거할 경우 예외가 발생한다.")
    @Test
    void deleteOnlySection() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));
        LineServiceRequest lineServiceRequest = new LineServiceRequest("2호선", "green", upStation.getId(), downStation.getId(), 10);
        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        assertThatThrownBy(() -> sectionService.delete(lineServiceResponse.getId(), downStation.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("노선에 존재하지 않는 역을 제거할 경우 예외가 발생한다.")
    @Test
    void deleteNotExistingStation() {
        Station station1 = stationDao.save(new Station("강남역"));
        Station station2 = stationDao.save(new Station("선릉역"));
        Station station3 = stationDao.save(new Station("잠실역"));
        Station station4 = stationDao.save(new Station("성수역"));
        LineServiceResponse lineServiceResponse = lineService.save(new LineServiceRequest(
                "2호선",
                "green",
                station1.getId(),
                station2.getId(),
                10));
        SectionServiceRequest sectionServiceRequest = new SectionServiceRequest(lineServiceResponse.getId(), station2.getId(), station3.getId(), 10);
        sectionService.connect(sectionServiceRequest);

        assertThatThrownBy(() -> sectionService.delete(lineServiceResponse.getId(), station4.getId()))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
