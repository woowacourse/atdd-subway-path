package wooteco.subway.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

import java.util.List;
import java.util.Objects;

@Transactional
@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(Long upStationId, Long downStationId) {
        validateNotSameStations(upStationId, downStationId);
        final GraphPath<Station, DefaultWeightedEdge> graphPath = findGraphPath(upStationId, downStationId);
        validatePathExist(graphPath);

        final List<Station> stations = graphPath.getVertexList();
        final int shortestDistance = (int) graphPath.getWeight();
        final Fare fare = Fare.from(shortestDistance);
        return new PathResponse(stations, shortestDistance, fare.getValue());
    }

    private void validateNotSameStations(Long upStationId, Long downStationId) {
        if (Objects.equals(upStationId, downStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> findGraphPath(Long upStationId, Long downStationId) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(initSubwayMap());

        final Station upStation = findStation(upStationId);
        final Station downStation = findStation(downStationId);
        return dijkstraShortestPath.getPath(upStation, downStation);
    }

    private void validatePathExist(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        if (graphPath == null) {
            throw new IllegalArgumentException("해당 역 사이 경로가 존재하지 않습니다.");
        }
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initSubwayMap() {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addAllStations(graph);
        addAllSections(graph);
        return graph;
    }

    private void addAllStations(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        final List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addAllSections(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        final List<Section> sections = sectionDao.findAll();
        for (Section section : sections) {
            final Station upStation = findStation(section.getUpStationId());
            final Station downStation = findStation(section.getDownStationId());
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }
    }

    private Station findStation(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다."));
    }
}
