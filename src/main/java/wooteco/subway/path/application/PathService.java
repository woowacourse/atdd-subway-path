package wooteco.subway.path.application;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        List<Station> stations = stationDao.findAll();
        List<Section> sections = sectionDao.findAll();

        GraphPath<Station, DefaultWeightedEdge> path = makeDijkstraShortestPath(stations, sections).getPath(stationDao.findById(sourceId), stationDao.findById(targetId));
        List<Station> shortestPath = path.getVertexList();

        return new PathResponse(StationResponse.listOf(shortestPath), (int) path.getWeight());
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> makeDijkstraShortestPath(final List<Station> stations, final List<Section> sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(graph, stations);
        setEdgeWeight(graph, sections);
        return new DijkstraShortestPath<>(graph);
    }

    private void addVertex(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void setEdgeWeight(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
