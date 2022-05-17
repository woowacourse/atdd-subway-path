package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.IllegalInputException;

class SubwayMapTest {

    @Test
    @DisplayName("동일한 역의 경로를 조회할 경우 예외를 던진다.")
    void GetPath_SameStations_ExceptionThrown() {
        // given
        final Line greenLine = new Line("2호선", "green");
        final Line yellowLine = new Line("수인분당선", "yellow");
        final Line orangeLine = new Line("3호선", "orange");

        final Station gangnam = new Station(1L, "강남역");
        final Station yeoksam = new Station(2L, "역삼역");
        final Station seolleung = new Station(3L, "선릉역");
        final Station samsung = new Station(4L, "삼성역");
        final Station seoulForest = new Station(5L, "서울숲역");
        final Station wangsimni = new Station(6L, "왕십리역");
        final Station yacksu = new Station(7L, "약수역");
        final Station geumho = new Station(8L, "금호역");
        final Station oksu = new Station(9L, "옥수역");

        final Section greenSectionA = new Section(greenLine, gangnam, yeoksam, new Distance(10));
        final Section greenSectionB = new Section(greenLine, yeoksam, seolleung, new Distance(10));
        final Section greenSectionC = new Section(greenLine, seolleung, samsung, new Distance(10));

        final Section yellowSectionA = new Section(yellowLine, seolleung, seoulForest, new Distance(10));
        final Section yellowSectionB = new Section(yellowLine, seoulForest, wangsimni, new Distance(10));

        final Section orangeSectionA = new Section(orangeLine, yacksu, geumho, new Distance(10));
        final Section orangeSectionB = new Section(orangeLine, geumho, oksu, new Distance(10));

        final Sections sections = new Sections(List.of(
                greenSectionA,
                greenSectionB,
                greenSectionC,
                yellowSectionA,
                yellowSectionB,
                orangeSectionA,
                orangeSectionB
        ));

        final SubwayMap subwayMap = new SubwayMap(sections);

        // when, then
        assertThatThrownBy(() -> subwayMap.getPath(gangnam, gangnam))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("출발역과 도착역이 동일합니다.");
    }
}
