package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.Fixtures.강남_역삼_구간;
import static wooteco.subway.Fixtures.강남역;
import static wooteco.subway.Fixtures.삼성_종합운동장_구간;
import static wooteco.subway.Fixtures.삼성역;
import static wooteco.subway.Fixtures.선릉_삼성_구간;
import static wooteco.subway.Fixtures.선릉역;
import static wooteco.subway.Fixtures.역삼_선릉_구간;
import static wooteco.subway.Fixtures.역삼역;
import static wooteco.subway.Fixtures.잠실새내_잠실_구간;
import static wooteco.subway.Fixtures.잠실새내역;
import static wooteco.subway.Fixtures.잠실역;
import static wooteco.subway.Fixtures.종합운동장_잠실새내_구간;
import static wooteco.subway.Fixtures.종합운동장역;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

class PathTest {

    private List<Section> sections;

    @BeforeEach
    void setUp() {
        sections = List.of(강남_역삼_구간, 역삼_선릉_구간, 선릉_삼성_구간, 삼성_종합운동장_구간, 종합운동장_잠실새내_구간, 잠실새내_잠실_구간);
    }

    @DisplayName("구간 리스트를 전달 받아 Path 객체를 생성한다.")
    @Test
    void constructor_withSectionList() {
        // given & when
        Path createdPath = new Path(sections, 강남역, 잠실역);

        // then
        assertThat(createdPath).isNotNull();
    }

    @DisplayName("getStations 를 호출하면 생성자에서 계산한 강남역에서 잠실역까지의 최단경로 역의 목록은 7개이다.")
    @Test
    void shortest_path() {
        // given
        Path createdPath = new Path(sections, 강남역, 잠실역);

        // when
        List<Station> actual = createdPath.getStations();

        // then
        assertThat(actual).containsExactly(강남역, 역삼역, 선릉역, 삼성역, 종합운동장역, 잠실새내역, 잠실역);
    }

    @DisplayName("getStations 를 호출하면 생성자에서 계산한 강남역에서 잠실새내역까지의 최단경로 역의 목록은 6개이다.")
    @Test
    void shortest_path_2() {
        // given
        Path createdPath = new Path(sections, 강남역, 잠실새내역);

        // when
        List<Station> actual = createdPath.getStations();

        // then
        assertThat(actual).containsExactly(강남역, 역삼역, 선릉역, 삼성역, 종합운동장역, 잠실새내역);
    }

    @DisplayName("강남역에서 잠실역까지의 요금은 2750원이다.")
    @Test
    void fare_of_shortest_path() {
        // given
        Path createdPath = new Path(sections, 강남역, 잠실역);

        // when
        int actual = createdPath.getFare().getValue();

        // then
        int expected = 2750;
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("출발역과 도착역을 동일하게 설정하면 예외가 발생한다.")
    @Test
    void throws_exception_if_departure_is_same_as_arrival() {
        // given
        Station departure = 강남역;
        Station arrival = 강남역;

        // when & then
        assertThatThrownBy(() -> new Path(sections, departure, arrival))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경로의 출발역과 도착역이 같을 수 없습니다.");
    }
}
