package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathServiceResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathServiceResponse findShortestPath(PathServiceRequest pathServiceRequest) {
        Sections sections = sectionDao.findAll();
        Long departureId = pathServiceRequest.getDepartureId();
        Long arrivalId = pathServiceRequest.getArrivalId();
        List<Long> shortestPathStationIds = sections.getShortestPathStationIds(departureId, arrivalId);
        List<Station> stations = shortestPathStationIds.stream()
                .map(stationDao::findById)
                .collect(Collectors.toList());

        int distance = sections.getShortestPathDistance(departureId, arrivalId);
        int fare = FareCalculator.getInstance().calculate(distance);
        return new PathServiceResponse(stations, distance, fare);
    }
}
