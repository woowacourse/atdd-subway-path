package wooteco.subway.admin.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
public class StationServiceTest {
    private static final String STATION_NAME_1 = "선릉";
    private static final String STATION_NAME_2 = "역삼";
    private static final String STATION_NAME_3 = "강남";
    private static final String STATION_NAME_4 = "양재";
    private static final String STATION_NAME_5 = "양재시민의숲";

    @Mock
    private StationRepository stationRepository;

    private StationService stationService;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;
    private Station station5;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, STATION_NAME_1);
        station2 = new Station(2L, STATION_NAME_2);
        station3 = new Station(3L, STATION_NAME_3);
        station4 = new Station(4L, STATION_NAME_4);
        station5 = new Station(5L, STATION_NAME_5);

        stationService = new StationService(stationRepository);
    }

    @Test
    void createStation() {
        Station station = new Station(6L, "잠실");
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실");
        when(stationRepository.save(any(Station.class))).thenReturn(station);

        StationResponse response = stationService.createStation(stationCreateRequest);

        assertThat(response.getName()).isEqualTo("잠실");
    }

    @Test
    void findAll() {
        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2, station3, station4, station5));

        List<StationResponse> response = stationService.findAll();

        assertThat(response.size()).isEqualTo(5);
    }
}
