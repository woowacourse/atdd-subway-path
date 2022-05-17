package wooteco.subway.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {


    private final LinkedList<Section> path;

    private Path(LinkedList<Section> path) {
        this.path = path;
    }

    public static Path from(Sections sections, long sourceId, long targetId) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        Set<Long> stationIds = new HashSet<>(sections.getStationsId());
        stationIds.forEach(graph::addVertex);
        sections.getSections().forEach(section -> {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        });

        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        List<Long> shortestPath
                = dijkstraShortestPath.getPath(sourceId, targetId).getVertexList();

        LinkedList<Section> path = new LinkedList<>();

        for (int i = 0; i < shortestPath.size() - 1; i++) {
            path.add(sections.findSection(shortestPath.get(i), shortestPath.get(i + 1)));
        }

        return new Path(path);
    }

    public int getTotalDistance() {
        return path.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }
}
