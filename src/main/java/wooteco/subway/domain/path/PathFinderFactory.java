package wooteco.subway.domain.path;

import wooteco.subway.domain.section.Sections;
import wooteco.subway.utils.JgraphtPathAlgorithm;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PathFinderFactory {

    private PathFinderFactory() {
    }

    public static PathFinder create(Sections sections) {
        Set<Long> stationIds = sections.distinctStationIds();
        List<SectionWeightedEdge> sectionWeightedEdges = getSectionWeightedEdges(sections);
        PathAlgorithm<Long, SectionWeightedEdge> jgraphtPathAlgorithm =
                new JgraphtPathAlgorithm<>(SectionWeightedEdge.class, stationIds, sectionWeightedEdges);
        return new PathFinder(jgraphtPathAlgorithm);
    }

    private static List<SectionWeightedEdge> getSectionWeightedEdges(Sections sections) {
        return sections.values().stream().
                map(SectionWeightedEdge::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
