package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    void create() {
        List<Section> sections = new ArrayList<>();

        assertThatNoException().isThrownBy(() -> new Path(new Sections(sections)));
    }

    @Test
    void createShortestPath() {
        List<Section> sections = new ArrayList<>();
        Line line = new Line("2호선", "bg-red-200");
        Station 강남역 = new Station(1L, "강남역");
        Station 선릉역 = new Station(3L, "선릉역");
        sections.add(new Section(line, 강남역, new Station(2L,"역삼역"), 10));
        sections.add(new Section(line, new Station(2L, "역삼역"), 선릉역, 10));
        sections.add(new Section(line, 선릉역, new Station(4L, "삼성역"), 10));

        Path path = new Path(new Sections(sections));
        List<Long> shortestPath = path.createShortestPath(강남역.getId(), 선릉역.getId());

        assertThat(shortestPath).containsExactly(1L, 2L, 3L);
    }

    @Test
    void calculateDistance() {
        List<Section> sections = new ArrayList<>();
        Line line = new Line("2호선", "bg-red-200");
        Station upStation = new Station(1L, "강남역");
        Station downStation = new Station(3L, "선릉역");
        sections.add(new Section(line, upStation, new Station(2L, "역삼역"), 10));
        sections.add(new Section(line, new Station(2L, "역삼역"), new Station(3L, "선릉역"), 10));
        sections.add(new Section(line, new Station(3L, "선릉역"), new Station(4L, "삼성역"), 10));

        Path path = new Path(new Sections(sections));
        List<Long> shortestPath = path.createShortestPath(upStation.getId(), downStation.getId());

        assertThat(path.calculateDistance(upStation.getId(), downStation.getId())).isEqualTo(20);

//        assertThat(shortestPath).containsExactly("강남역", "역삼역", "선릉역");
    }
}
