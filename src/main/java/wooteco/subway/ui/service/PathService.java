package wooteco.subway.ui.service;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.ShortestPathFactory;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse getPath(Long source, Long target) {
        List<Section> sections = sectionDao.findAll();
        List<Station> stations = stationDao.findAll();
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath =
                ShortestPathFactory.getFrom(stations, sections);

        Station sourceStation = stationDao.findById(source);
        Station targetStation = stationDao.findById(target);
        Path path = Path.from(shortestPath, sourceStation, targetStation);

        return new PathResponse(StationResponse.of(path.getStations()), path.getDistance(), path.calculateFare());
    }
}
