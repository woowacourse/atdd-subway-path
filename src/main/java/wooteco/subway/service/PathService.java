package wooteco.subway.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, LineDao lineDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(Long upStationId, Long downStationId, int age) {
        validateNotSameStations(upStationId, downStationId);
        final GraphPath<Station, ShortestPathEdge> graphPath = findGraphPath(upStationId, downStationId);
        validatePathExist(graphPath);

        final List<Station> stations = graphPath.getVertexList();
        final int shortestDistance = (int) graphPath.getWeight();
        final int extraFare = findMaximumExtraFare(graphPath);
        final Fare fare = Fare.of(shortestDistance, extraFare, age);
        return new PathResponse(stations, shortestDistance, fare.getValue());
    }

    private void validateNotSameStations(Long upStationId, Long downStationId) {
        if (Objects.equals(upStationId, downStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private GraphPath<Station, ShortestPathEdge> findGraphPath(Long upStationId, Long downStationId) {
        final DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(initSubwayMap());

        final Station upStation = findStation(upStationId);
        final Station downStation = findStation(downStationId);
        return dijkstraShortestPath.getPath(upStation, downStation);
    }

    private void validatePathExist(GraphPath<Station, ShortestPathEdge> graphPath) {
        if (graphPath == null) {
            throw new IllegalArgumentException("해당 역 사이 경로가 존재하지 않습니다.");
        }
    }

    private WeightedMultigraph<Station, ShortestPathEdge> initSubwayMap() {
        final WeightedMultigraph<Station, ShortestPathEdge> graph
                = new WeightedMultigraph<>(ShortestPathEdge.class);
        addAllStations(graph);
        addAllSections(graph);
        return graph;
    }

    private void addAllStations(WeightedMultigraph<Station, ShortestPathEdge> graph) {
        final List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addAllSections(WeightedMultigraph<Station, ShortestPathEdge> graph) {
        final List<Section> sections = sectionDao.findAll();
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

    private int findMaximumExtraFare(GraphPath<Station, ShortestPathEdge> graphPath) {
        return graphPath.getEdgeList().stream()
                .map(ShortestPathEdge::getLineId)
                .map(lineId -> lineDao.findById(lineId)
                        .orElseThrow(() -> new NoSuchElementException("경로 라인을 찾는 과정 중 오류가 발생했습니다.")))
                .max(Comparator.comparingInt(Line::getExtraFare))
                .orElseThrow(() -> new NoSuchElementException("추가 요금을 찾는 과정 중 오류가 발생했습니다."))
                .getExtraFare();
    }
}
