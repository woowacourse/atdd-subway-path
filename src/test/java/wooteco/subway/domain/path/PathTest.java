package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.LineExtraFare;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 선릉역 = new Station(2L, "선릉역");
    private static final Station 잠실역 = new Station(3L, "잠실역");
    private static final Station 강변역 = new Station(4L, "강변역");
    private static final Station 역삼역 = new Station(5L, "역삼역");
    private static final Station 양재역 = new Station(6L, "양재역");
    private static final Section 선릉_잠실_먼_구간 = new Section(1L, 선릉역, 잠실역, 100);
    private static final Section 강남_선릉_가까운_구간 = new Section(1L, 강남역, 선릉역, 1);
    private static final Section 선릉_강변_가까운_구간 = new Section(2L, 선릉역, 강변역, 2);
    private static final Section 선릉_역삼_먼_구간 = new Section(3L, 선릉역, 역삼역, 100);
    private static final Section 잠실_양재_가까운_구간 = new Section(1L, 잠실역, 양재역, 6);
    private static final Section 강변_역삼_가까운_구간 = new Section(2L, 강변역, 역삼역, 3);
    private static final Section 역삼_잠실_가까운_구간 = new Section(3L, 역삼역, 잠실역, 5);
    private static final Navigator NAVIGATOR = new Navigator(List.of(강남_선릉_가까운_구간, 선릉_잠실_먼_구간,
            선릉_강변_가까운_구간, 선릉_역삼_먼_구간, 잠실_양재_가까운_구간, 강변_역삼_가까운_구간, 역삼_잠실_가까운_구간));

    @Test
    void toStations_메서드는_시작점부터_목적지까지의_최단경로에_해당하는_지하철역들을_순서대로_제공() {
        Path path = getPathOf(선릉역, 잠실역, NAVIGATOR);

        List<Station> actual = path.toStations();
        List<Station> expected = List.of(선릉역, 강변역, 역삼역, 잠실역);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDistance_메서드는_최단경로의_거리를_반환() {
        Path path = getPathOf(선릉역, 잠실역, NAVIGATOR);

        int actual = path.getDistance();
        int expected = 10;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getPassingLineIds_메서드는_최단거리의_구간들이_속해있는_모든_노선들의_id를_반환() {
        Path path = getPathOf(선릉역, 잠실역, NAVIGATOR);

        List<Long> actual = path.getPassingLineIds();
        List<Long> expected = List.of(2L, 3L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 노선에_구간으로_등록되지_않은_역에_대한_경로를_조회하려는_경우_예외_발생() {
        Station 미등록_역 = new Station(999L, "등록되지 않은 역");
        Navigator navigator = new Navigator(List.of(new Section(강남역, 선릉역, 10),
                new Section(선릉역, 잠실역, 100), new Section(잠실역, 강변역, 20)));

        assertThatThrownBy(() -> getPathOf(강남역, 미등록_역, navigator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출발점과_도착점이_동일한_경우_예외_발생() {
        Navigator navigator = new Navigator(List.of(new Section(강남역, 선릉역, 10)));

        assertThatThrownBy(() -> getPathOf(강남역, 강남역, navigator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 도달할_수_없는_경로를_조회하려는_경우_예외_발생() {
        Navigator navigator = new Navigator(List.of(
                new Section(강남역, 선릉역, 10), new Section(잠실역, 강변역, 20)));

        assertThatThrownBy(() -> getPathOf(강남역, 잠실역, navigator))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("calculateFare 메서드는 [기본요금 + 거리 추가비용 + 노선 추가비용]에 나이 할인을 적용한 요금을 반환한다.")
    @Test
    void calculateFare() {
        Section 거리_12짜리_강남_선릉_구간 = new Section(1L, 강남역, 선릉역, 12);
        Navigator navigator = new Navigator(List.of(거리_12짜리_강남_선릉_구간));
        Path path = new Path(강남역, 선릉역, navigator);

        int actual = path.calculateFare(List.of(new LineExtraFare(200)),15);
        int expected = (int) ((((1250 + 100) + 200) - 350) * 0.8);

        assertThat(actual).isEqualTo(expected);
    }

    private Path getPathOf(Station start, Station target, Navigator navigator) {
        return new Path(start, target, navigator);
    }
}
