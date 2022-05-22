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
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.LineServiceResponse;
import wooteco.subway.service.dto.StationServiceResponse;

@SpringBootTest
class LineServiceTest extends DBTest {

    private final StationDao stationDao;
    private final LineService lineService;

    private Station upStation;
    private Station downStation;
    private LineServiceRequest lineServiceRequest;

    @Autowired
    public LineServiceTest(StationDao stationDao, LineService lineService) {
        this.stationDao = stationDao;
        this.lineService = lineService;
    }

    @BeforeEach
    void setUp() {
        upStation = stationDao.save(new Station("강남역"));
        downStation = stationDao.save(new Station("선릉역"));
        lineServiceRequest = new LineServiceRequest(
                "2호선", "green", upStation.getId(), downStation.getId(), 10, 200);
    }

    @DisplayName("노선을 저장한다.")
    @Test
    void save() {
        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);
        assertAll(
                () -> assertThat(lineServiceResponse.getName()).isEqualTo(lineServiceRequest.getName()),
                () -> assertThat(lineServiceResponse.getColor()).isEqualTo(lineServiceRequest.getColor()),
                () -> assertThat(lineServiceResponse.getExtraFare()).isEqualTo(lineServiceRequest.getExtraFare())
        );
    }

    @DisplayName("같은 이름의 노선을 저장하는 경우 예외가 발생한다.")
    @Test
    void saveExistingName() {
        lineService.save(lineServiceRequest);

        assertThatThrownBy(() -> lineService.save(lineServiceRequest))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 노선을 조회한다.")
    @Test
    void findAll() {
        Station upStation2 = stationDao.save(new Station("교대역"));
        Station downStation2 = stationDao.save(new Station("수서역"));

        LineServiceRequest lineServiceRequest2 = new LineServiceRequest(
                "3호선", "orange", upStation2.getId(), downStation2.getId(), 10, 300);

        lineService.save(lineServiceRequest);
        lineService.save(lineServiceRequest2);

        List<LineServiceResponse> lineServiceResponses = lineService.findAll();
        LineServiceResponse firstResponse = lineServiceResponses.get(0);
        LineServiceResponse secondResponse = lineServiceResponses.get(1);
        List<StationServiceResponse> firstLineStations = firstResponse.getStations();
        List<StationServiceResponse> secondResponseStations = secondResponse.getStations();

        assertAll(
                () -> assertThat(firstResponse.getName()).isEqualTo(lineServiceRequest.getName()),
                () -> assertThat(firstResponse.getColor()).isEqualTo(lineServiceRequest.getColor()),
                () -> assertThat(firstResponse.getExtraFare()).isEqualTo(lineServiceRequest.getExtraFare()),
                () -> assertThat(firstLineStations.get(0).getName()).isEqualTo(upStation.getName()),
                () -> assertThat(firstLineStations.get(1).getName()).isEqualTo(downStation.getName()),

                () -> assertThat(secondResponse.getName()).isEqualTo(lineServiceRequest2.getName()),
                () -> assertThat(secondResponse.getColor()).isEqualTo(lineServiceRequest2.getColor()),
                () -> assertThat(secondResponse.getExtraFare()).isEqualTo(lineServiceRequest2.getExtraFare()),
                () -> assertThat(secondResponseStations.get(0).getName()).isEqualTo(upStation2.getName()),
                () -> assertThat(secondResponseStations.get(1).getName()).isEqualTo(downStation2.getName())
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findById() {
        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        LineServiceResponse foundLine = lineService.findById(lineServiceResponse.getId());

        assertAll(
                () -> assertThat(foundLine.getName()).isEqualTo(lineServiceResponse.getName()),
                () -> assertThat(foundLine.getColor()).isEqualTo(lineServiceResponse.getColor()),
                () -> assertThat(foundLine.getExtraFare()).isEqualTo(lineServiceResponse.getExtraFare())
        );
    }

    @DisplayName("존재하지 않는 지하철 노선을 조회할 경우 예외가 발생한다.")
    @Test
    void findNotExistingLine() {
        assertThatThrownBy(() -> lineService.findById(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void update() {
        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        LineServiceRequest updateLineServiceRequest = new LineServiceRequest(
                "3호선", "orange", null, null, 0, 500);
        lineService.update(lineServiceResponse.getId(), updateLineServiceRequest);
        LineServiceResponse foundLine = lineService.findById(lineServiceResponse.getId());

        assertAll(
                () -> assertThat(foundLine.getName()).isEqualTo(updateLineServiceRequest.getName()),
                () -> assertThat(foundLine.getColor()).isEqualTo(updateLineServiceRequest.getColor()),
                () -> assertThat(foundLine.getExtraFare()).isEqualTo(updateLineServiceRequest.getExtraFare())
        );
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteById() {
        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        lineService.deleteById(lineServiceResponse.getId());

        assertThat(lineService.findAll().size()).isZero();
    }
}
