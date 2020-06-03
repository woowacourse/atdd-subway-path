package wooteco.subway.admin.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.errors.PathException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StationServiceTest {
    private static final String STATION_NAME_KANGNAM = "강남역";
    @Autowired
    private StationRepository stationRepository;

    private StationService stationSerivce;

    @BeforeEach
    void setUp() {
        stationRepository.deleteAll();
        stationSerivce = new StationService(stationRepository);
    }

    @Test
    @DisplayName("저장된 지하철역을 이름으로 찾아낸다 ")
    void findByNameSuccessTest() {
        stationSerivce.save(new Station(STATION_NAME_KANGNAM));
        Station station = stationSerivce.findByName(STATION_NAME_KANGNAM);

        assertThat(station.getName()).isEqualTo(STATION_NAME_KANGNAM);
    }

    @Test
    @DisplayName("저장된 지하철역 이름 이외 의 이름일 경우 익셉션이 발생한다. ")
    void findByNameFailTest() {
        stationSerivce.save(new Station(STATION_NAME_KANGNAM));

        Assertions.assertThatThrownBy(() -> stationSerivce.findByName("다른역이름"))
                .isInstanceOf(PathException.class)
                .hasMessage("해당 역을 찾을 수 없습니다.");
    }

}
