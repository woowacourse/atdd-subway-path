package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.IllegalInputException;
import wooteco.subway.exception.path.NoSuchPathException;

class SubwayMapTest {

    private SubwayMap subwayMap;
    private Station gangnam;
    private Station yeoksam;
    private Station seolleung;
    private Station samsung;
    private Station seoulForest;
    private Station wangsimni;
    private Station yacksu;
    private Station geumho;
    private Station oksu;

    @BeforeEach
    void setUpData() {
        gangnam = new Station(1L, "강남역");
        yeoksam = new Station(2L, "역삼역");
        seolleung = new Station(3L, "선릉역");
        samsung = new Station(4L, "삼성역");
        seoulForest = new Station(5L, "서울숲역");
        wangsimni = new Station(6L, "왕십리역");
        yacksu = new Station(7L, "약수역");
        geumho = new Station(8L, "금호역");
        oksu = new Station(9L, "옥수역");

        final Line greenLine = new Line("2호선", "green");
        final Line yellowLine = new Line("수인분당선", "yellow");
        final Line orangeLine = new Line("3호선", "orange");

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

        subwayMap = new SubwayMap(sections);
    }

    @Test
    @DisplayName("출발역과 도착익이 동일한 경로를 조회할 경우 예외를 던진다.")
    void GetPath_SameStations_ExceptionThrown() {
        assertThatThrownBy(() -> subwayMap.searchPath(gangnam, gangnam))
                .isInstanceOf(IllegalInputException.class)
                .hasMessage("출발역과 도착역이 동일합니다.");
    }

    @Test
    @DisplayName("경로를 찾을 수 없는 경우 예외를 던진다.")
    void GetPath_InvalidPath_ExceptionThrown() {
        assertThatThrownBy(() -> subwayMap.searchPath(gangnam, oksu))
                .isInstanceOf(NoSuchPathException.class);
    }
}
