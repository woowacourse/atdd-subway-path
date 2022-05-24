package wooteco.subway.domain.path;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.exception.NotFoundPathException;
import wooteco.subway.exception.NotFoundStationException;

public class Path {

    private final LinkedList<Section> path;


    private Path(final LinkedList<Section> path) {
        this.path = path;
    }

    public static Path of(final Sections sections, final long sourceId, final long targetId,
                          final PathFindStrategy pathFindStrategy) {
        validateMovement(sourceId, targetId);
        final List<Long> shortestPath = pathFindStrategy.findPath(sections, sourceId, targetId);

        final LinkedList<Section> path = toSections(sections, shortestPath);
        return new Path(path);
    }

    private static void validateMovement(final long sourceId, final long targetId) {
        if (sourceId == targetId) {
            throw new NotFoundPathException("같은 위치로는 경로를 찾을 수 없습니다.");
        }
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
