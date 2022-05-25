package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.FareCalculator;
import wooteco.subway.domain.fare.FareCondition;
import wooteco.subway.domain.shortestpath.DistanceShortestPathStrategy;
import wooteco.subway.domain.shortestpath.ShortestPath;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;
    private final FareCalculator fareCalculator;

    public PathService(LineDao lineDao, SectionDao sectionDao, StationService stationService,
                       FareCalculator fareCalculator) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
        this.fareCalculator = fareCalculator;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(PathRequest pathRequest) {
        ShortestPath shortestPath = createShortestPath(pathRequest);
        Fare fare = calculateFare(pathRequest, shortestPath);

        List<Long> stationIds = shortestPath.getStationIds(pathRequest.getSource(), pathRequest.getTarget());
        List<StationResponse> stations = stationService.findByStationIds(stationIds);
        return new PathResponse(stations, shortestPath.getTotalDistance(), fare.getValue());
    }

    private ShortestPath createShortestPath(PathRequest pathRequest) {
        validateExistStations(pathRequest);
        List<Section> allSections = sectionDao.findAll();
        return new ShortestPath(new DistanceShortestPathStrategy(), new Sections(allSections),
                pathRequest.getSource(), pathRequest.getTarget());
    }

    private void validateExistStations(PathRequest pathRequest) {
        stationService.validateExistById(pathRequest.getSource());
        stationService.validateExistById(pathRequest.getTarget());
    }

    private Fare calculateFare(PathRequest pathRequest, ShortestPath shortestPath) {
        FareCondition fareCondition = new FareCondition(shortestPath.getTotalDistance(), getLines(shortestPath),
                pathRequest.getAge());
        return fareCalculator.calculate(fareCondition);
    }

    private List<Line> getLines(ShortestPath shortestPath) {
        List<Long> lineIds = shortestPath.getLineIds();
        return lineDao.findByIds(lineIds);
    }
}
