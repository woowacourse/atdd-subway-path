package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.TestFixture.강남역;
import static wooteco.subway.TestFixture.교대역;
import static wooteco.subway.TestFixture.선릉역;
import static wooteco.subway.TestFixture.역삼역;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import wooteco.subway.exception.DuplicateStationNameException;
import wooteco.subway.repository.JdbcStationRepository;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.ui.dto.response.StationResponse;

@TestConstructor(autowireMode = AutowireMode.ALL)
@JdbcTest
class StationServiceTest {

    private final StationService stationService;

    public StationServiceTest(DataSource dataSource) {
        this.stationService = new SpringStationService(new JdbcStationRepository(new StationDao(dataSource)));
    }

    @DisplayName("이름으로 지하철 역을 저장한다")
    @Test
    void create() {
        // given
        final String name = "강남역";

        // when
        final StationResponse stationResponse = stationService.create(name);

        // then
        assertAll(
                () -> assertThat(stationResponse.getId()).isGreaterThan(0L),
                () -> assertThat(stationResponse.getName()).isEqualTo(name)
        );
    }

    @DisplayName("이미 존재하는 이름으로 지하철역 생성 시도 시 예외가 발생한다")
    @Test
    void createStationWithDuplicatedNameShouldFail() {
        // given
        final String name = "강남역";
        stationService.create(name);

        // when & then
        assertThatThrownBy(() -> stationService.create(name))
                .isInstanceOf(DuplicateStationNameException.class)
                .hasMessageContaining("해당 이름의 지하철역은 이미 존재합니다.");
    }

    @DisplayName("지하철 역 목록을 조회한다")
    @Test
    void findAll() {
        // given
        stationService.create(강남역.getName());
        stationService.create(교대역.getName());
        stationService.create(역삼역.getName());
        stationService.create(선릉역.getName());

        // when
        final List<StationResponse> stations = stationService.findAll();

        assertAll(
                () -> assertThat(stations.size()).isEqualTo(4),
                () -> assertThat(stations).extracting("name")
                        .containsExactly(강남역.getName(), 교대역.getName(), 역삼역.getName(), 선릉역.getName())
        );
    }

    @DisplayName("ID로 지하철역을 삭제한다")
    @Test
    void deleteStationById() {
        // given
        final StationResponse saved = stationService.create(강남역.getName());
        final List<StationResponse> beforeDelete = stationService.findAll();

        // when
        stationService.deleteById(saved.getId());
        final List<StationResponse> afterDelete = stationService.findAll();

        // then
        assertAll(
                () -> assertThat(beforeDelete.size()).isOne(),
                () -> assertThat(afterDelete.size()).isZero()
        );
    }
}
