package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionSeriesSorter;
import wooteco.subway.domain.station.Station;

public class Path {

    private static final SectionSeriesSorter SORTER = new SectionSeriesSorter();

    private final List<Section> sections;

    public Path(List<Section> sections) {
        this.sections = sections;
    }

    public static Path of(PathFinder finder, Station sourceStation, Station targetStation) {
         List<Section> sections = finder.findShortestSections(sourceStation, targetStation);
    }
}
