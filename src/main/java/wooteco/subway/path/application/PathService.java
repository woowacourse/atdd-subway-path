package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.dto.SectionRequest;
import wooteco.subway.path.domain.Graph;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final Graph graph;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.graph = new Graph();
        initializeGraph();
    }

    public PathResponse shortestDistancePath(Long source, Long target) {
        Station sourceStation = stationDao.findById(source);
        Station targetStation = stationDao.findById(target);

        return getPathResponse(sourceStation, targetStation);
    }

    private void initializeGraph() {
        List<Section> sections = sectionDao.findAll();
        graph.initialize(sections);
    }

    private PathResponse getPathResponse(Station sourceStation, Station targetStation) {
        List<Station> shortestPath = graph.shortestPath(sourceStation, targetStation);
        int shortestDistance = graph.shortestDistance(sourceStation, targetStation);

        return new PathResponse(
                StationResponse.listOf(shortestPath),
                shortestDistance
        );
    }

    public void addSectionInfo(Station upStation, Station downStation, int distance) {
        graph.addSectionInfo(upStation, downStation, distance);
    }
}
