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
        sections.add(new Section(line, new Station("강남역"), new Station("역삼역"), 10));
        sections.add(new Section(line, new Station("역삼역"), new Station("선릉역"), 10));
        sections.add(new Section(line, new Station("선릉역"), new Station("삼성역"), 10));

        Path path = new Path(new Sections(sections));
        List<String> shortestPath = path.createShortestPath(new Station("강남역"), new Station("선릉역"));

        assertThat(shortestPath).containsExactly("강남역", "역삼역", "선릉역");
    }

    @Test
    void calculateDistance() {
        List<Section> sections = new ArrayList<>();
        Line line = new Line("2호선", "bg-red-200");
        Station upStation = new Station("강남역");
        Station downStation = new Station("선릉역");
        sections.add(new Section(line, upStation, new Station("역삼역"), 10));
        sections.add(new Section(line, new Station("역삼역"), new Station("선릉역"), 10));
        sections.add(new Section(line, new Station("선릉역"), new Station("삼성역"), 10));

        Path path = new Path(new Sections(sections));
        List<String> shortestPath = path.createShortestPath(upStation, new Station("선릉역"));

        assertThat(path.calculateDistance(upStation, downStation)).isEqualTo(20);

//        assertThat(shortestPath).containsExactly("강남역", "역삼역", "선릉역");
    }
}
