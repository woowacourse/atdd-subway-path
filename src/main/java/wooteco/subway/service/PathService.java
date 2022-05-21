package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationService stationService;

    public PathService(final SectionDao sectionDao, final StationService stationService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
    public PathResponse findShortestPath(final PathRequest pathRequest) {
        validateExistStations(pathRequest);
        final Path shortestPath = createPath(pathRequest);
        final Fare fare = Fare.from(shortestPath.getTotalDistance());

        final List<Long> stationIds = shortestPath.getStationIds(pathRequest.getSource(), pathRequest.getTarget());
        final List<StationResponse> stations = stationService.findByStationIds(stationIds);
        return new PathResponse(stations, shortestPath.getTotalDistance(), fare.getValue());
    }

    private void validateExistStations(final PathRequest pathRequest) {
        stationService.validateExistById(pathRequest.getSource());
        stationService.validateExistById(pathRequest.getTarget());
    }

    private Path createPath(final PathRequest pathRequest) {
        final List<Section> allSections = sectionDao.findAll();
        return Path.of(new Sections(allSections), pathRequest.getSource(), pathRequest.getTarget());
    }
}
