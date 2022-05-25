package wooteco.subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class SubwayMapTest {

    private final Line 신분당선 = new Line(1L, "신분당선", "색상", 1000);
    private final Line 분당선 = new Line(2L, "분당선", "색상", 0);
    private final Line 수인선 = new Line(3L, "수인선", "색상", 900);

    private final Station 강남역 = new Station(1L, "강남역");
    private final Station 선릉역 = new Station(2L, "선릉역");
    private final Station 잠실역 = new Station(3L, "잠실역");

    @DisplayName("생성자 유효성 검정 테스트")
    @Nested
    class InitTest {

        @DisplayName("노선이 전혀 존재하지 않는 경우도 존재하므로, 빈 배열을 받으면 예외 미발생")
        @Test
        void emptyListAllowed() {
            assertThatNoException()
                    .isThrownBy(() -> SubwayMap.of(List.of(), List.of()));
       }

        @Test
        void 노선_정보에_해당되지_않는_구간이_존재하는_경우_예외_발생() {
            List<Line> lines = List.of(신분당선);
            Section line1Section = new Section(1L, 강남역, 선릉역, 10);
            Section line2Section = new Section(2L, 강남역, 선릉역, 10);
            List<Section> lineSections = List.of(line1Section, line2Section);

            assertThatThrownBy(() -> SubwayMap.of(lines, lineSections))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        void 노선_정보들_중_등록된_구간이_전혀_존재하지_않는_경우_예외_발생() {
            List<Line> lines = List.of(신분당선, 분당선);
            Section line1Section = new Section(1L, 강남역, 선릉역, 10);
            List<Section> lineSections = List.of(line1Section);

            assertThatThrownBy(() -> SubwayMap.of(lines, lineSections))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    void toSortedLines_메서드는_노선의_id_순서대로_정렬된_노선들을_반환() {
        List<Line> lineInfos = List.of(신분당선, 분당선, 수인선);
        Section line1Section1To2 = new Section(1L, 강남역, 선릉역, 10);
        Section line1Section2To3 = new Section(1L, 선릉역, 잠실역, 30);
        Section line2Section1To2 = new Section(2L, 강남역, 선릉역, 10);
        Section line3Section = new Section(3L, 선릉역, 잠실역, 30);
        List<Section> lineSections = List.of(line2Section1To2, line1Section1To2, line1Section2To3, line3Section);

        SubwayMap subwayMap = SubwayMap.of(lineInfos, lineSections);
        List<LineMap> actual = subwayMap.toSortedLines();
        List<LineMap> expected = List.of(
                new LineMap(신분당선, new Sections(List.of(line1Section1To2, line1Section2To3))),
                new LineMap(분당선, new Sections(List.of(line2Section1To2))),
                new LineMap(수인선, new Sections(List.of(line3Section))));

        assertThat(actual).isEqualTo(expected);
    }
}
