package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    private Station 강남역;
    private Station 역삼역;
    private Station 선릉역;
    private Station 삼성역;
    private Station 종합운동장역;
    private Station 잠실새내역;
    private Station 잠실역;

    private List<Section> sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        선릉역 = new Station("선릉역");
        삼성역 = new Station("삼성역");
        종합운동장역 = new Station("종합운동장역");
        잠실새내역 = new Station("잠실새내역");
        잠실역 = new Station("잠실역");

        Section 강남_역삼 = Section.createWithoutId(강남역, 역삼역, 5);
        Section 역삼_선릉 = Section.createWithoutId(역삼역, 선릉역, 5);
        Section 선릉_삼성 = Section.createWithoutId(선릉역, 삼성역, 5);
        Section 삼성_종합운동장 = Section.createWithoutId(삼성역, 종합운동장역, 5);
        Section 종합운동장_잠실새내 = Section.createWithoutId(종합운동장역, 잠실새내역, 5);
        Section 잠실새내_잠실 = Section.createWithoutId(잠실새내역, 잠실역, 5);

        sections = List.of(강남_역삼, 역삼_선릉, 선릉_삼성, 삼성_종합운동장, 종합운동장_잠실새내, 잠실새내_잠실);
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

    @DisplayName("강남역에서 잠실역까지의 요금은 1650원이다.")
    @Test
    void fare_of_shortest_path() {
        // given
        Path createdPath = new Path(sections, 강남역, 잠실역);

        // when
        int actual = createdPath.getFare().getValue();

        // then
        int expected = 1650;
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
