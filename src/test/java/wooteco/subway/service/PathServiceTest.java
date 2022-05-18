package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.path.PathResponse;

class PathServiceTest extends ServiceTest {

    @InjectMocks
    private PathService pathService;

    @Test
    @DisplayName("출발역과 도착역의 최단 경로에 대한 정보를 조회한다.")
    void Find() {
        // given
        Station gangnam = new Station(1L, "강남역");
        Station yeoksam = new Station(2L, "역삼역");
        Station seolleung = new Station(3L, "선릉역");
        Station samsung = new Station(4L, "삼성역");
        Station seoulForest = new Station(5L, "서울숲역");
        Station wangsimni = new Station(6L, "왕십리역");
        Station yacksu = new Station(7L, "약수역");
        Station geumho = new Station(8L, "금호역");
        Station oksu = new Station(9L, "옥수역");

        final Line greenLine = new Line("2호선", "green");
        final Line yellowLine = new Line("수인분당선", "yellow");
        final Line orangeLine = new Line("3호선", "orange");

        final Section greenSectionA = new Section(greenLine, gangnam, yeoksam, new Distance(10));
        final Section greenSectionB = new Section(greenLine, yeoksam, seolleung, new Distance(7));
        final Section greenSectionC = new Section(greenLine, seolleung, samsung, new Distance(11));

        final Section yellowSectionA = new Section(yellowLine, seolleung, seoulForest, new Distance(3));
        final Section yellowSectionB = new Section(yellowLine, seoulForest, wangsimni, new Distance(8));

        final Section orangeSectionA = new Section(orangeLine, yacksu, geumho, new Distance(12));
        final Section orangeSectionB = new Section(orangeLine, geumho, oksu, new Distance(6));

        final Sections sections = new Sections(List.of(
                greenSectionA,
                greenSectionB,
                greenSectionC,
                yellowSectionA,
                yellowSectionB,
                orangeSectionA,
                orangeSectionB
        ));

        given(stationDao.findById(any(Long.class)))
                .willReturn(Optional.of(gangnam))
                .willReturn(Optional.of(seoulForest));

        given(sectionDao.findAll())
                .willReturn(sections);

        final List<Station> expectedStations = List.of(
                gangnam,
                yeoksam,
                seolleung,
                seoulForest
        );
        final Distance expectedDistance = new Distance(20);
        final PathResponse expected = PathResponse.of(expectedStations, expectedDistance, new Fare(1450));

        // when
        final PathResponse actual = pathService.find(gangnam.getId(), seoulForest.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
