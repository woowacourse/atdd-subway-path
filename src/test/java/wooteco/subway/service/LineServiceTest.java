package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import wooteco.subway.acceptance.DBTest;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineCreationServiceRequest;
import wooteco.subway.service.dto.LineModificationServiceRequest;
import wooteco.subway.service.dto.LineServiceResponse;
import wooteco.subway.service.dto.StationServiceResponse;

@SpringBootTest
class LineServiceTest extends DBTest {

    private final StationDao stationDao;
    private final LineService lineService;

    private Station upStation;
    private Station downStation;
    private LineCreationServiceRequest lineCreationServiceRequest;

    @Autowired
    public LineServiceTest(StationDao stationDao, LineService lineService) {
        this.stationDao = stationDao;
        this.lineService = lineService;
    }

    @BeforeEach
    void setUp() {
        upStation = stationDao.save(new Station("강남역"));
        downStation = stationDao.save(new Station("선릉역"));
        lineCreationServiceRequest = new LineCreationServiceRequest(
                "2호선", "green", upStation.getId(), downStation.getId(), 10, 200);
    }

    @DisplayName("노선을 저장한다.")
    @Test
    void save() {
        LineServiceResponse lineServiceResponse = lineService.save(lineCreationServiceRequest);
        assertAll(
                () -> assertThat(lineServiceResponse.getName()).isEqualTo(lineCreationServiceRequest.getName()),
                () -> assertThat(lineServiceResponse.getColor()).isEqualTo(lineCreationServiceRequest.getColor()),
                () -> assertThat(lineServiceResponse.getExtraFare())
                        .isEqualTo(lineCreationServiceRequest.getExtraFare())
        );
    }

    @DisplayName("같은 이름의 노선을 저장하는 경우 예외를 발생시킨다.")
    @Test
    void save_exception_duplicatedName() {
        lineService.save(lineCreationServiceRequest);

        assertThatThrownBy(() -> lineService.save(lineCreationServiceRequest))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("노선의 추가 요금이 100원 보다 적다면 예외를 발생시킨다.")
    @Test
    void create_exception_extraFareLowerThanMinValue() {
        LineCreationServiceRequest lineCreationServiceRequest = new LineCreationServiceRequest(
                "2호선", "green", upStation.getId(), downStation.getId(), 10, 99);

        assertThatThrownBy(() -> lineService.save(lineCreationServiceRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최소 추가 금액은 100원입니다.");
    }

    @DisplayName("모든 지하철 노선을 조회한다.")
    @Test
    void findAll() {
        Station upStation2 = stationDao.save(new Station("교대역"));
        Station downStation2 = stationDao.save(new Station("수서역"));

        LineCreationServiceRequest lineCreationServiceRequest2 = new LineCreationServiceRequest(
                "3호선", "orange", upStation2.getId(), downStation2.getId(), 10, 300);

        lineService.save(lineCreationServiceRequest);
        lineService.save(lineCreationServiceRequest2);

        List<LineServiceResponse> lineServiceResponses = lineService.findAll();
        LineServiceResponse firstResponse = lineServiceResponses.get(0);
        LineServiceResponse secondResponse = lineServiceResponses.get(1);
        List<StationServiceResponse> firstLineStations = firstResponse.getStations();
        List<StationServiceResponse> secondResponseStations = secondResponse.getStations();

        assertAll(
                () -> assertThat(firstResponse.getName()).isEqualTo(lineCreationServiceRequest.getName()),
                () -> assertThat(firstResponse.getColor()).isEqualTo(lineCreationServiceRequest.getColor()),
                () -> assertThat(firstResponse.getExtraFare()).isEqualTo(lineCreationServiceRequest.getExtraFare()),
                () -> assertThat(firstLineStations.get(0).getName()).isEqualTo(upStation.getName()),
                () -> assertThat(firstLineStations.get(1).getName()).isEqualTo(downStation.getName()),

                () -> assertThat(secondResponse.getName()).isEqualTo(lineCreationServiceRequest2.getName()),
                () -> assertThat(secondResponse.getColor()).isEqualTo(lineCreationServiceRequest2.getColor()),
                () -> assertThat(secondResponse.getExtraFare()).isEqualTo(lineCreationServiceRequest2.getExtraFare()),
                () -> assertThat(secondResponseStations.get(0).getName()).isEqualTo(upStation2.getName()),
                () -> assertThat(secondResponseStations.get(1).getName()).isEqualTo(downStation2.getName())
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findById() {
        LineServiceResponse lineServiceResponse = lineService.save(lineCreationServiceRequest);

        LineServiceResponse foundLine = lineService.findById(lineServiceResponse.getId());

        assertAll(
                () -> assertThat(foundLine.getName()).isEqualTo(lineServiceResponse.getName()),
                () -> assertThat(foundLine.getColor()).isEqualTo(lineServiceResponse.getColor()),
                () -> assertThat(foundLine.getExtraFare()).isEqualTo(lineServiceResponse.getExtraFare())
        );
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회할 경우 예외가 발생한다.")
    @Test
    void find_exception_nonExistingLineId() {
        assertThatThrownBy(() -> lineService.findById(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void update() {
        LineServiceResponse lineServiceResponse = lineService.save(lineCreationServiceRequest);
        LineModificationServiceRequest updateLineCreationServiceRequest =
                new LineModificationServiceRequest("3호선", "orange", 500);

        lineService.update(lineServiceResponse.getId(), updateLineCreationServiceRequest);
        LineServiceResponse foundLine = lineService.findById(lineServiceResponse.getId());

        assertAll(
                () -> assertThat(foundLine.getName()).isEqualTo(updateLineCreationServiceRequest.getName()),
                () -> assertThat(foundLine.getColor()).isEqualTo(updateLineCreationServiceRequest.getColor()),
                () -> assertThat(foundLine.getExtraFare()).isEqualTo(updateLineCreationServiceRequest.getExtraFare())
        );
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteById() {
        LineServiceResponse lineServiceResponse = lineService.save(lineCreationServiceRequest);

        lineService.deleteById(lineServiceResponse.getId());

        assertThat(lineService.findAll().size()).isZero();
    }

    @DisplayName("노선 id들을 받아 노선들의 추가 운임 비용 중 가장 높은 비용을 반환한다.")
    @Test
    void findHighestExtraFareByIds() {
        LineServiceResponse firstLine = lineService.save(lineCreationServiceRequest);
        LineServiceResponse secondLine = lineService.save(new LineCreationServiceRequest(
                "3호선", "orange", upStation.getId(), downStation.getId(), 10, 300));
        LineServiceResponse thirdLine = lineService.save(new LineCreationServiceRequest(
                "4호선", "skyBlue", upStation.getId(), downStation.getId(), 10, 400));

        int highestExtraFare =
                lineService
                        .findHighestExtraFareByIds(List.of(firstLine.getId(), secondLine.getId(), thirdLine.getId()));
        //then
        assertThat(highestExtraFare).isEqualTo(400);
    }
}
