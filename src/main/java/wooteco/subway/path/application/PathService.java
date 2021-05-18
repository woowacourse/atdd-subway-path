package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.StationNotFoundException;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.SectionGraph;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse find(Long source, Long target) {
        Sections sections = sectionDao.findAll();
        List<Station> allStations = stationDao.findAll();
        Station departureStation = findStationById(allStations, source);
        Station arrivalStation = findStationById(allStations, target);

        SectionGraph sectionGraph = sections.sectionGraph(allStations, departureStation, arrivalStation);
        List<Station> shortestStations = sectionGraph.shortestPathOfStations();
        List<StationResponse> stationResponses = shortestStations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, sectionGraph.shortestDistance());
    }

    private Station findStationById(List<Station> stations, Long id) {
        return stations.stream()
                .filter(station -> station.isSameId(id))
                .findAny()
                .orElseThrow(StationNotFoundException::new);
    }
}
