package wooteco.subway.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.service.PathServiceRequest;
import wooteco.subway.dto.service.PathServiceResponse;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.strategy.DistanceFareStrategy;
import wooteco.subway.strategy.FareStrategy;

@Service
public class PathService {

    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 역입니다.";

    private final StationDao stationDao;
    private final DomainCreatorService domainCreatorService;

    public PathService(StationDao stationDao,
        DomainCreatorService domainCreatorService) {
        this.stationDao = stationDao;
        this.domainCreatorService = domainCreatorService;
    }

    public PathServiceResponse getShortestPath(PathServiceRequest pathServiceRequest) {
        validateExists(pathServiceRequest.getSource());
        validateExists(pathServiceRequest.getTarget());

        Station source = stationDao.getStation(pathServiceRequest.getSource());
        Station target = stationDao.getStation(pathServiceRequest.getTarget());

        Path path = domainCreatorService.createPath();
        List<StationDto> stationDtos = path.getShortestPath(source, target).stream()
            .map(station -> new StationDto(station.getId(), station.getName()))
            .collect(Collectors.toList());
        int distance = path.getShortestDistance(source, target);
        FareStrategy fare = new DistanceFareStrategy();

        return new PathServiceResponse(stationDtos, distance, fare.calculate(distance));
    }

    private void validateExists(Long id) {
        if (!stationDao.existById(id)) {
            throw new NoSuchElementException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }
}
