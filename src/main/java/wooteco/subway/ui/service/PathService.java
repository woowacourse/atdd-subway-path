package wooteco.subway.ui.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;

import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Age;
import wooteco.subway.domain.path.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathAlgorithm;
import wooteco.subway.domain.section.Section;

import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

import wooteco.subway.support.ShortestPath;

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
        PathAlgorithm pathAlgorithm = makePathAlgorithmFrom(sections);

        Station sourceStation = stationDao.findById(pathRequest.getSource());
        Station targetStation = stationDao.findById(pathRequest.getTarget());
        Path path = pathAlgorithm.getPath(sourceStation, targetStation);
        Fare fare = path.calculateFare(new Age(pathRequest.getAge()));

        return new PathResponse(StationResponse.of(path.getStations()), path.getDistance(), fare.getValue());
    }

    private PathAlgorithm makePathAlgorithmFrom(List<Section> sections) {
        Map<Section, Fare> edges = sections.stream()
                .collect(Collectors.toMap(
                        (section) -> section,
                        (section) -> sectionDao.findExtraFareById(section.getId())
                ));
        return new ShortestPath(edges);
    }
}
