package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.ShortestPath;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Stations;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse findShortestPath(PathServiceRequest pathRequest) {
        ShortestPath shortestPath = getShortestPath();

        List<StationResponse> stations = getShortestPathStations(pathRequest, shortestPath);
        Fare fare = new Fare();
        int shortestDistance = shortestPath.findShortestDistance(pathRequest.getSource(),
            pathRequest.getTarget());
        int fee = fare.calculate(shortestDistance);

        return new PathResponse(stations, shortestDistance, fee);
    }

    private ShortestPath getShortestPath() {
        Sections sections = new Sections(sectionDao.findAll());
        return new ShortestPath(sections);
    }

    private List<StationResponse> getShortestPathStations(PathServiceRequest pathRequest, ShortestPath shortestPath) {
        List<Long> stationIds = shortestPath.findShortestPath(pathRequest.getSource(),
            pathRequest.getTarget());
        Stations stations = new Stations(stationDao.findById(stationIds));
        return toStationServiceResponse(stations.sortedStationsById(stationIds));
    }

    private List<StationResponse> toStationServiceResponse(List<Station> stations) {
        return stations.stream()
            .map(i -> new StationResponse(i.getId(), i.getName()))
            .collect(Collectors.toList());
    }
}
