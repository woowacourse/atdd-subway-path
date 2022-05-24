package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@SpringBootTest
@Transactional
class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private StationDao stationDao;

    @DisplayName("역을 생성한다.")
    @Test
    void create() {
        // given
        final StationResponse stationResponse = stationService.create(new StationRequest("아차산역"));

        // then
        assertAll(
                () -> assertThat(stationResponse.getId()).isNotNull(),
                () -> assertThat(stationResponse.getName()).isEqualTo("아차산역")
        );
    }

    @DisplayName("역을 생성할 때 이미 존재하는 역이름을 사용하면 예외를 발생한다.")
    @Test
    void thrown_duplicateName() {
        // given
        stationDao.save(new Station("잠실역"));

        // then
        assertThatThrownBy(() -> stationService.create(new StationRequest("잠실역")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 같은 이름의 지하철역이 존재합니다.");
    }

    @DisplayName("모든 역을 불러온다.")
    @Test
    void getAll() {
        // given
        stationDao.save(new Station("잠실역"));

        // when
        stationService.create(new StationRequest("아차산역"));

        // then
        final List<StationResponse> stations = stationService.getAll();

        assertThat(stations.size()).isEqualTo(2);
    }

    @DisplayName("지하철역을 id로 삭제한다.")
    @Test
    void remove() {
        // given
        Station station = stationDao.save(new Station("아차산역"));

        // when
        stationService.remove(station.getId());
        final List<StationResponse> stations = stationService.getAll();

        // then
        assertThat(stations.size()).isZero();
    }
}
