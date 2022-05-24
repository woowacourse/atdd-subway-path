package wooteco.subway.ui.service;

import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.path.Age;
import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathEdge;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.path.ShortestPathFactory;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
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

    public PathResponse getPath(PathRequest pathRequest) {
        List<Section> sections = sectionDao.findAll();
        Map<Section, Fare> edges = sections.stream()
                .collect(Collectors.toMap(
                        (section) -> section,
                        (section) -> sectionDao.findExtraFareById(section.getId())
                ));
        DijkstraShortestPath<Station, PathEdge> shortestPath = ShortestPathFactory.getFrom(edges);

        Station sourceStation = stationDao.findById(pathRequest.getSource());
        Station targetStation = stationDao.findById(pathRequest.getTarget());
        Path path = Path.from(shortestPath, sourceStation, targetStation);
        Fare fare = path.calculateFare(new Age(pathRequest.getAge()));

        return new PathResponse(StationResponse.of(path.getStations()), path.getDistance(), fare.getValue());
    }
}
