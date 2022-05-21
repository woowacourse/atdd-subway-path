package wooteco.subway.acceptance.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.application.PathService;
import wooteco.subway.application.StationService;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private StationService stationService;
    @Mock
    private SectionDao sectionDao;

    @DisplayName("최단 경로와 거리에 비례한 요금이 정확힌지 확인")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "11,1350", "16,1450","50,2050", "51,2150", "58, 2150", "59,2250"})
    void getPath(int distance, int expectedCost) {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");

        final List<Section> sections = List.of(Section.createWithoutId(강남역, 역삼역, distance));

        given(sectionDao.findAll()).willReturn(sections);
        doReturn(강남역).when(stationService).findStationById(1L);
        doReturn(역삼역).when(stationService).findStationById(2L);
        // when
        PathResponse pathResponse = pathService.getPath(1L, 2L);
        // then
        final List<String> stationNames = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(pathResponse.getFare()).isEqualTo(expectedCost),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(distance),
                () -> assertThat(stationNames).containsExactly("강남역", "역삼역")
        );
    }

    @DisplayName("존재하지 않는 경로를 조회하면 예외 발생")
    @Test
    void getNonExistentPath() {
        //given
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Station 삼성역 = new Station("삼성역");

        final List<Section> sections = List.of(Section.createWithoutId(강남역, 역삼역, 10));

        given(sectionDao.findAll()).willReturn(sections);
        doReturn(강남역).when(stationService).findStationById(1L);
        doReturn(삼성역).when(stationService).findStationById(3L);
        //then
        assertThatThrownBy(() -> pathService.getPath(1L, 3L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
