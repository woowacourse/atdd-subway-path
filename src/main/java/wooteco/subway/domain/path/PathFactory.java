package wooteco.subway.domain.path;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PathFactory {

    public Path makePath(Long source, Long target, List<Long> stationIds, Sections sections) {
        validateExistStationId(source, target, stationIds);
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = makeGraph(stationIds, sections);
        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Long, DefaultWeightedEdge> path = dijkstraShortestPath.getPath(source, target);
        validateExistPath(path);

        List<Long> shortestPathByStationId = path.getVertexList();
        int totalDistance = (int) path.getWeight();
        Sections shortestSections = getShortestSections(sections, shortestPathByStationId);
        return new Path(shortestPathByStationId, totalDistance, shortestSections);
    }

    private void validateExistStationId(Long source, Long target, List<Long> stationIds) {
        if (isNotContains(source, target, stationIds)) {
            throw new IllegalArgumentException("[ERROR] 역을 찾을 수 없습니다");
        }
    }

    private boolean isNotContains(Long source, Long target, List<Long> stationIds) {
        return !(stationIds.contains(source) && stationIds.contains(target));
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> makeGraph(List<Long> stationId, Sections sections) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(stationId, graph);
        addEdges(sections, graph);
        return graph;
    }

    private void addVertexes(List<Long> stationId, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Long id : stationId) {
            graph.addVertex(id);
        }
    }

    private void addEdges(Sections sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStationId(), section.getDownStationId()), section.getDistance());
        }
    }

    private void validateExistPath(GraphPath<Long, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalArgumentException("[ERROR] 경로를 찾을 수 없습니다");
        }
    }

    private Sections getShortestSections(Sections sections, List<Long> shortestPath) {
        List<Section> shortestSections = new ArrayList<>();
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Long upStationId = shortestPath.get(i);
            Long downStationId = shortestPath.get(i + 1);
            List<Section> findSections = sections.getSections().stream()
                    .filter(it -> (it.containStationId(upStationId, downStationId)))
                    .collect(Collectors.toList());
            if (findSections.isEmpty()) {
                continue;
            }
            if (findSections.size() == 1) {
                shortestSections.addAll(findSections);
                continue;
            }
            addShortestSections(shortestSections, findSections);
        }
        return new Sections(shortestSections);
    }

    private void addShortestSections(List<Section> shortestSections, List<Section> findSections) {
        Section shortestSection = findSections.get(0);
        for (Section section : findSections) {
            if (shortestSection.getDistance() > section.getDistance()) {
                shortestSection = section;
            }
        }
        shortestSections.add(shortestSection);
    }
}
