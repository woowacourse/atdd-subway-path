package wooteco.subway.service.pathInfra;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestPathFinder implements PathFinder {
    private final StationDao stationDao;

    public ShortestPathFinder(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Path findShortestPath(List<Section> sections, Long sourceId, Long targetId) {
        final WeightedMultigraph<Station, ShortestPathEdge> graph
                = new WeightedMultigraph<>(ShortestPathEdge.class);
        addAllStations(graph);
        addAllSections(graph, sections);

        final Station source = findStation(sourceId);
        final Station target = findStation(targetId);
        final GraphPath<Station, ShortestPathEdge> graphPath = new DijkstraShortestPath<>(graph).getPath(source, target);
        validatePathExist(graphPath);
        return makePath(graphPath);
    }

    private void addAllStations(WeightedMultigraph<Station, ShortestPathEdge> graph) {
        List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addAllSections(WeightedMultigraph<Station, ShortestPathEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            final Station upStation = findStation(section.getUpStationId());
            final Station downStation = findStation(section.getDownStationId());
            final Long lineId = section.getLineId();
            final int distance = section.getDistance();
            graph.addEdge(upStation, downStation, new ShortestPathEdge(lineId, distance));
        }
    }

    private Station findStation(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다."));
    }

    private void validatePathExist(GraphPath<Station, ShortestPathEdge> graphPath) {
        if (graphPath == null) {
            throw new IllegalArgumentException("해당 역 사이 경로가 존재하지 않습니다.");
        }
    }

    private Path makePath(GraphPath<Station, ShortestPathEdge> graphPath) {
        final List<Station> stations = graphPath.getVertexList();
        final List<Long> lineIds = graphPath.getEdgeList()
                .stream()
                .map(ShortestPathEdge::getLineId)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
        final int distance = (int) graphPath.getWeight();
        return new Path(stations, lineIds, distance);
    }
}
