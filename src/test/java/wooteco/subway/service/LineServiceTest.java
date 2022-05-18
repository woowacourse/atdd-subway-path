package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineServiceRequest;
import wooteco.subway.service.dto.LineServiceResponse;

@SpringBootTest
class LineServiceTest extends ServiceTest {

    private final StationDao stationDao;
    private final LineService lineService;

    @Autowired
    public LineServiceTest(StationDao stationDao, LineService lineService) {
        this.stationDao = stationDao;
        this.lineService = lineService;
    }

    @DisplayName("노선을 저장한다.")
    @Test
    void save() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));

        LineServiceRequest lineServiceRequest = new LineServiceRequest("2호선", "green", upStation.getId(), downStation.getId(), 10);

        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        assertThat(lineServiceResponse.getName()).isEqualTo(lineServiceRequest.getName());
    }

    @DisplayName("같은 이름의 노선을 저장하는 경우 예외가 발생한다.")
    @Test
    void saveExistingName() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));

        LineServiceRequest lineServiceRequest = 
                new LineServiceRequest("2호선", "green", upStation.getId(), downStation.getId(), 10);

        lineService.save(lineServiceRequest);

        assertThatThrownBy(() -> lineService.save(lineServiceRequest))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 노선을 조회한다.")
    @Test
    void findAll() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));

        LineServiceRequest lineServiceRequest1 = 
                new LineServiceRequest("2호선", "green", upStation.getId(), downStation.getId(), 10);

        Station upStation2 = stationDao.save(new Station("교대역"));
        Station downStation2 = stationDao.save(new Station("수서역"));

        LineServiceRequest lineServiceRequest2 = 
                new LineServiceRequest("3호선", "orange", upStation2.getId(), downStation2.getId(), 10);

        lineService.save(lineServiceRequest1);
        lineService.save(lineServiceRequest2);

        List<String> lineNames = lineService.findAll()
                .stream()
                .map(LineServiceResponse::getName)
                .collect(Collectors.toList());

        assertThat(lineNames).containsExactly("2호선", "3호선");
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findById() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));

        LineServiceRequest lineServiceRequest = 
                new LineServiceRequest("2호선", "green", upStation.getId(), downStation.getId(), 10);

        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        LineServiceResponse foundLine = lineService.findById(lineServiceResponse.getId());

        assertThat(foundLine.getName()).isEqualTo(lineServiceResponse.getName());
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
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));

        LineServiceRequest lineServiceRequest = 
                new LineServiceRequest("2호선", "green", upStation.getId(), downStation.getId(), 10);
        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        lineService.update(lineServiceResponse.getId(), "3호선", "orange");

        assertThat(lineService.findById(lineServiceResponse.getId()).getName()).isEqualTo("3호선");
    }

    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteById() {
        Station upStation = stationDao.save(new Station("강남역"));
        Station downStation = stationDao.save(new Station("선릉역"));

        LineServiceRequest lineServiceRequest = 
                new LineServiceRequest("2호선", "green", upStation.getId(), downStation.getId(), 10);
        LineServiceResponse lineServiceResponse = lineService.save(lineServiceRequest);

        lineService.deleteById(lineServiceResponse.getId());

        assertThat(lineService.findAll().size()).isZero();
    }
}
