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

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse shortestDistancePath(Long source, Long target) {
        List<Section> sectionList = sectionDao.findAll();
        Station sourceStation = stationDao.findById(source);
        Station targetStation = stationDao.findById(target);

        return getPathResponse(sectionList, sourceStation, targetStation);
    }

    private PathResponse getPathResponse(List<Section> sectionList, Station sourceStation, Station targetStation) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        for (Section section : sectionList) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation,downStation), section.getDistance());
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(sourceStation, targetStation);
        List<Station> shortestPath = path.getVertexList();

        return new PathResponse(
                StationResponse.listOf(shortestPath),
                (int) path.getWeight()
        );
    }
}
