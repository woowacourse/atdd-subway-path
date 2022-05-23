package wooteco.subway.domain.path;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.notfound.NotFoundPathException;
import wooteco.subway.exception.notfound.NotFoundStationInSectionException;

@Component
public class JGraphTPathFinder implements PathFinder {

    @Override
    public Path searchShortestPath(Sections sections, Station source, Station target) {
        validateSourceAndTargetExist(sections.getStations(), source, target);
        final GraphPath<Station, SectionEdge> path = setupPath(sections, source, target);

        final List<Station> stationsInPath = path.getVertexList();
        final long distance = (long) path.getWeight();
        final List<Long> lineIds = findLineIdsThrough(path);

        return new Path(stationsInPath, distance, lineIds);
    }

    private void validateSourceAndTargetExist(List<Station> stations, Station source, Station target) {
        if (!stations.contains(source)) {
            throw new NotFoundStationInSectionException(source.getId());
        }

        if (!stations.contains(target)) {
            throw new NotFoundStationInSectionException(target.getId());
        }
    }

    private GraphPath<Station, SectionEdge> setupPath(Sections sections,
                                                      Station source, Station target) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        addStations(sections.getStations(), graph);
        addSections(sections.getSections(), graph);

        final GraphPath<Station, SectionEdge> path = new DijkstraShortestPath<>(graph).getPath(source, target);
        validatePathExists(source, target, path);

        return path;
    }

    private void addStations(List<Station> stations, WeightedMultigraph<Station, SectionEdge> graph) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addSections(List<Section> sections, WeightedMultigraph<Station, SectionEdge> graph) {
        for (Section section : sections) {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    new SectionEdge(section.getLineId(), section.getDistance()));
        }
    }

    private void validatePathExists(Station source, Station target, GraphPath<Station, SectionEdge> path) {
        if (Objects.isNull(path)) {
            throw new NotFoundPathException(source.getId(), target.getId());
        }
    }

    private List<Long> findLineIdsThrough(GraphPath<Station, SectionEdge> path) {
        return path.getEdgeList()
                .stream()
                .map(SectionEdge::getLineId)
                .distinct()
                .collect(Collectors.toList());
    }
}
