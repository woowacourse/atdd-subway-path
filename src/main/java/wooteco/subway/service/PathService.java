package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.shortestpath.DistanceShortestPathStrategy;
import wooteco.subway.domain.shortestpath.ShortestPath;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public PathService(SectionDao sectionDao, StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(PathRequest pathRequest) {
        validateExistStations(pathRequest);
        List<Section> allSections = sectionDao.findAll();
        ShortestPath shortestPath = new ShortestPath(new DistanceShortestPathStrategy(), new Sections(allSections),
                pathRequest.getSource(), pathRequest.getTarget());
        Fare fare = Fare.from(shortestPath.getTotalDistance());
        List<Long> stationIds = shortestPath.getStationIds(pathRequest.getSource(), pathRequest.getTarget());
        List<StationResponse> stations = stationService.findByStationIds(stationIds);
        return new PathResponse(stations, shortestPath.getTotalDistance(), fare.getValue());
    }

    private void validateExistStations(PathRequest pathRequest) {
        stationService.validateExistById(pathRequest.getSource());
        stationService.validateExistById(pathRequest.getTarget());
    }
}
