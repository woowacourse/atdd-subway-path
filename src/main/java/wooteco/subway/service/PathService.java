package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.info.PathServiceRequest;
import wooteco.subway.dto.info.PathServiceResponse;
import wooteco.subway.dto.info.StationDto;

@Service
public class PathService {

    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 역입니다.";

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final DomainCreatorService domainCreatorService;

    public PathService(SectionDao sectionDao, StationDao stationDao,
        DomainCreatorService domainCreatorService) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.domainCreatorService = domainCreatorService;
    }

    public PathServiceResponse getShortestPath(PathServiceRequest pathServiceRequest) {
        validateNotExists(pathServiceRequest.getSource());
        validateNotExists(pathServiceRequest.getTarget());

        Station source = stationDao.getStation(pathServiceRequest.getSource());
        Station target = stationDao.getStation(pathServiceRequest.getTarget());

        Path path = domainCreatorService.createPath();
        List<StationDto> stationDtos = path.getShortestPath(source, target).stream()
            .map(station -> new StationDto(station.getId(), station.getName()))
            .collect(Collectors.toList());
        int distance = path.getShortestDistance(source, target);

        return new PathServiceResponse(stationDtos, distance);
    }

    private void validateNotExists(Long id) {
        if (!stationDao.existById(id)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }
}
