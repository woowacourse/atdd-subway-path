package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {
    Station 강남역;
    Station 역삼역;
    Station 선릉역;
    Station 삼성역;
    Station 종합운동장역;
    Station 잠실새내역;
    Station 잠실역;
    private List<Station> stations;
    private List<Section> sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        역삼역 = new Station(2L, "역삼역");
        선릉역 = new Station(3L, "선릉역");
        삼성역 = new Station(4L, "삼성역");
        종합운동장역 = new Station(5L, "종합운동장역");
        잠실새내역 = new Station(6L, "잠실새내역");
        잠실역 = new Station(7L, "잠실역");

        stations = List.of(강남역, 역삼역, 선릉역, 삼성역, 종합운동장역, 잠실새내역, 잠실역);

        Section 강남_역삼 = Section.createWithoutId(강남역, 역삼역, 10);
        Section 역삼_선릉 = Section.createWithoutId(역삼역, 선릉역, 10);
        Section 선릉_삼성 = Section.createWithoutId(선릉역, 삼성역, 10);
        Section 삼성_종합운동장 = Section.createWithoutId(삼성역, 종합운동장역, 10);
        Section 종합운동장_잠실새내 = Section.createWithoutId(종합운동장역, 잠실새내역, 10);
        Section 잠실새내_잠실 = Section.createWithoutId(잠실새내역, 잠실역, 10);

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
}
