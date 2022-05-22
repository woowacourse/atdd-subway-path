package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Name;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.path.PathResponse;

class PathServiceTest extends ServiceTest {

    @InjectMocks
    private PathService pathService;

    @ParameterizedTest
    @DisplayName("출발역과 도착역의 최단 경로에 대한 정보를 조회한다.")
    @CsvSource(value = {"6:700", "12:700", "13:1120", "18:1120", "19:1750"}, delimiter = ':')
    void Find(final int age, final int expectedFare) {
        // given
        final Station gangnam = new Station(1L, "강남역");
        final Station yeoksam = new Station(2L, "역삼역");
        final Station seolleung = new Station(3L, "선릉역");
        final Station samsung = new Station(4L, "삼성역");

        final Station seoulForest = new Station(5L, "서울숲역");
        final Station wangsimni = new Station(6L, "왕십리역");

        final Station yacksu = new Station(7L, "약수역");
        final Station geumho = new Station(8L, "금호역");
        final Station oksu = new Station(9L, "옥수역");

        Line greenLine = new Line(1L, new Name("2호선"), "green", 100);
        final Section greenSectionA = new Section(greenLine, gangnam, yeoksam, new Distance(10));
        final Section greenSectionB = new Section(greenLine, yeoksam, seolleung, new Distance(7));
        final Section greenSectionC = new Section(greenLine, seolleung, samsung, new Distance(11));
        greenLine = greenLine.addSections(new Sections(List.of(
                greenSectionA,
                greenSectionB,
                greenSectionC
        )));

        Line yellowLine = new Line(2L, new Name("수인분당선"), "yellow", 300);
        final Section yellowSectionA = new Section(yellowLine, seolleung, seoulForest, new Distance(3));
        final Section yellowSectionB = new Section(yellowLine, seoulForest, wangsimni, new Distance(8));
        yellowLine = yellowLine.addSections(new Sections(List.of(
                yellowSectionA,
                yellowSectionB
        )));

        Line orangeLine = new Line(3L, new Name("3호선"), "orange", 500);
        final Section orangeSectionA = new Section(orangeLine, yacksu, geumho, new Distance(12));
        final Section orangeSectionB = new Section(orangeLine, geumho, oksu, new Distance(6));
        orangeLine = orangeLine.addSections(new Sections(List.of(
                orangeSectionA,
                orangeSectionB
        )));

        given(stationDao.findById(any(Long.class)))
                .willReturn(Optional.of(gangnam))
                .willReturn(Optional.of(seoulForest));

        final List<Line> lines = List.of(
                greenLine,
                yellowLine,
                orangeLine
        );
        given(lineDao.findAll())
                .willReturn(lines);

        final List<Station> expectedStations = List.of(
                gangnam,
                yeoksam,
                seolleung,
                seoulForest
        );
        final Distance expectedDistance = new Distance(20);
        final PathResponse expected = PathResponse.of(expectedStations, expectedDistance, new Fare(expectedFare));

        // when
        final PathResponse actual = pathService.find(gangnam.getId(), seoulForest.getId(), age);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
