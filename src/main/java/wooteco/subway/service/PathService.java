package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    private final PathFinder pathFinder;

    public PathService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao, PathFinder pathFinder) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(Long source, Long target, Long age) {
        Sections sections = new Sections(sectionDao.findAll());
        Path path = sections.findShortestPath(pathFinder, source, target);
        Fare fare = new Fare(path.getDistance(), findExtraFareOfPath(path), age);
        return new PathResponse(findStationsOfPath(path), path.getDistance(), fare.calculateFare());
    }

    private List<Station> findStationsOfPath(Path path) {
        return path.getStationIds().stream()
                .map(stationDao::findById)
                .collect(Collectors.toList());
    }

    private int findExtraFareOfPath(Path path) {
        return path.getLineIds().stream()
                .mapToInt(it -> lineDao.findById(it).getExtraFare())
                .max().getAsInt();
    }
}
