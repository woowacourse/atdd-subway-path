package wooteco.subway.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.NotFoundPathException;
import wooteco.subway.exception.NotFoundStationException;

public class Path {

    private final LinkedList<Section> path;


    private Path(final LinkedList<Section> path) {
        this.path = path;
    }

    public static Path of(final Sections sections, final long sourceId, final long targetId) {
        validateMovement(sourceId, targetId);
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph = getSubwayGraph(sections);

        final DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        final List<Long> shortestPath = findShortestPath(sourceId, targetId, dijkstraShortestPath);

        final LinkedList<Section> path = toSections(sections, shortestPath);
        return new Path(path);
    }

    private static void validateMovement(final long sourceId, final long targetId) {
        if (sourceId == targetId) {
            throw new NotFoundPathException("같은 위치로는 경로를 찾을 수 없습니다.");
        }
    }

    private static List<Long> findShortestPath(final long sourceId, final long targetId,
                                               final DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath) {

        try {
            return shortestPath.getPath(sourceId, targetId).getVertexList();
        } catch (NullPointerException exception) {
            throw new NotFoundPathException("현재 구간으로 해당 지하철역을 갈 수 없습니다.");
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            throw new NotFoundStationException("해당 지하철역이 등록이 안되어 있습니다.");
        }
    }

    private static WeightedMultigraph<Long, DefaultWeightedEdge> getSubwayGraph(final Sections sections) {
        final WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        final List<Long> stationIds = sections.getStationIds();
        stationIds.forEach(graph::addVertex);

        sections.getSections().forEach(
                section -> graph.setEdgeWeight(
                        graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                        section.getDistance()
                )
        );
        return graph;
    }

    private static LinkedList<Section> toSections(final Sections sections, final List<Long> shortestPath) {
        final LinkedList<Section> path = new LinkedList<>();

        for (int i = 0; i < shortestPath.size() - 1; i++) {
            path.add(sections.findSection(shortestPath.get(i), shortestPath.get(i + 1)));
        }
        return path;
    }

    public int getTotalDistance() {
        return path.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public List<Long> getStationIds(long sourceId, final long targetId) {
        final List<Long> stationIds = new LinkedList<>();
        for (Section section : path) {
            stationIds.add(sourceId);
            sourceId = section.getOppositeStation(sourceId);
        }
        stationIds.add(targetId);
        return stationIds;
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(path);
    }
}
