package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationRequest;

class StationServiceTest extends ServiceMockTest {

    @InjectMocks
    private StationService stationService;

    @Test
    @DisplayName("역을 생성한다.")
    void create() {
        // given
        StationRequest request = new StationRequest("강남역");
        Station 강남역 = new Station(1L, "강남역");
        when(stationService.create(request)).thenReturn(강남역);

        // when
        Station actual = stationService.create(request);

        // then
        assertThat(actual.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("모든 역을 조회한다.")
    void findAll() {
        // given
        Station 노원역 = new Station(1L, "노원역");
        Station 하계역 = new Station(2L, "하계역");
        when(stationRepository.findAll()).thenReturn(List.of(노원역, 하계역));
        // when
        List<Station> stations = stationService.findAll();

        // then
        assertThat(stations).hasSize(2);
    }
}
