package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Service
public class PathService {

    private final SectionService sectionService;
    private final StationService stationService;
    private final LineService lineService;

    public PathService(SectionService sectionService, StationService stationService,
                       LineService lineService) {
        this.sectionService = sectionService;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(PathRequest pathRequest) {
        validateExistStations(pathRequest);

        Path shortestPath = Path.of(new Sections(
            sectionService.findAll()), pathRequest.getSource(), pathRequest.getTarget());
        Fare fare = Fare.from(
            shortestPath.getTotalDistance(), lineService.getMaxExtraFare(shortestPath.getLineIds()));

        return new PathResponse(
            getStationResponses(pathRequest, shortestPath),
            shortestPath.getTotalDistance(),
            fare.getValue());
    }

    private void validateExistStations(PathRequest pathRequest) {
        stationService.validateExistById(pathRequest.getSource());
        stationService.validateExistById(pathRequest.getTarget());
    }

    private List<StationResponse> getStationResponses(PathRequest pathRequest, Path shortestPath) {
        List<Long> stationIds =
            shortestPath.getStationIds(pathRequest.getSource(), pathRequest.getTarget());

        return stationService.findByStationIds(stationIds);
    }
}
