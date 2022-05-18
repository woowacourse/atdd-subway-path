package wooteco.subway.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathFindResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathService {

    private static final int BASIS_FARE = 1_250;
    private static final int BASIC_FARE_OVER_50KM = 2_050;
    private static final int FIRST_FARE_INCREASE_STANDARD = 10;
    private static final int LAST_FARE_INCREASE_STANDARD = 50;
    private static final int FIRST_FARE_INCREASE_STANDARD_UNIT = 5;
    private static final int LAST_FARE_INCREASE_STANDARD_UNIT = 8;
    private static final int INCREASE_RATE = 100;

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathFindResult findPath(Long from, Long to) {
        List<Section> sections = sectionDao.findAll();
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = createSubwayGraph(sections);

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        List<Long> shortestPath = dijkstraShortestPath.getPath(from, to).getVertexList();

        int distance = (int) dijkstraShortestPath.getPathWeight(from, to);
        int fare = calculateFare(distance);
        List<Station> shortestPathStation = stationDao.findByIdIn(shortestPath);

        return PathFindResult.of(shortestPathStation, distance, fare);
    }

    public int calculateFare(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException();
        }
        if (distance <= FIRST_FARE_INCREASE_STANDARD) {
            return BASIS_FARE;
        }
        if (distance <= LAST_FARE_INCREASE_STANDARD) {
            return BASIS_FARE + INCREASE_RATE *
                    (int) Math.ceil((double) (distance - FIRST_FARE_INCREASE_STANDARD) / FIRST_FARE_INCREASE_STANDARD_UNIT);
        }
        return BASIC_FARE_OVER_50KM + INCREASE_RATE *
                (int) Math.ceil((double) (distance - LAST_FARE_INCREASE_STANDARD) / LAST_FARE_INCREASE_STANDARD_UNIT);
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> createSubwayGraph(List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        Set<Long> stationIds = findNonDuplicateStationIds(sections);
        fillVertexes(graph, stationIds);
        fillEdges(sections, graph);
        return graph;
    }

    private Set<Long> findNonDuplicateStationIds(List<Section> sections) {
        Set<Long> stationIds = new HashSet<>();
        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }
        return stationIds;
    }

    private void fillVertexes(WeightedMultigraph<Long, DefaultWeightedEdge> graph, Set<Long> stationIds) {
        for (Long stationId : stationIds) {
            graph.addVertex(stationId);
        }
    }

    private void fillEdges(List<Section> sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            Long upStationId = section.getUpStationId();
            Long downStationId = section.getDownStationId();

            DefaultWeightedEdge edge = graph.addEdge(upStationId, downStationId);
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }
}
