package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;

    @DisplayName("거리가 10Km일 때 요금은 1250원 이다.")
    @ParameterizedTest
    @CsvSource(value = {"10,1250", "15,1350", "58,2150"})
    void getPath(int distance, int expectedFare) {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");

        final List<Section> sections = List.of(Section.createWithoutId(강남역, 역삼역, distance));

        given(sectionDao.findAll()).willReturn(sections);
        doReturn(강남역).when(stationDao).findById(1L);
        doReturn(역삼역).when(stationDao).findById(2L);
        // when
        PathResponse pathResponse = pathService.getPath(1L, 2L);
        // then
        assertAll(
                () -> assertThat(pathResponse.getFare()).isEqualTo(expectedFare),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(distance)
        );
    }
}
