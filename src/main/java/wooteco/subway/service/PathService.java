package wooteco.subway.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.converter.PathConverter;
import wooteco.subway.dto.service.PathServiceRequest;
import wooteco.subway.dto.service.PathServiceResponse;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.strategy.DistanceFareStrategy;
import wooteco.subway.strategy.FareStrategy;
import wooteco.subway.utils.DijkstraShortestPathStation;

@Service
public class PathService {

    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 역입니다.";

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathServiceResponse getShortestPath(PathServiceRequest pathServiceRequest) {
        validateExists(pathServiceRequest.getSource());
        validateExists(pathServiceRequest.getTarget());

        Station source = stationDao.getStation(pathServiceRequest.getSource());
        Station target = stationDao.getStation(pathServiceRequest.getTarget());

        List<SectionEntity> sectionEntities = sectionDao.findAll();
        Sections sections = getSections(sectionEntities);
        Path path = DijkstraShortestPathStation.getPath(sections, source, target);
        List<StationDto> stationDtos = path.getStations().stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());

        FareStrategy fare = new DistanceFareStrategy();
        return new PathServiceResponse(stationDtos, path.getDistance(),
                fare.calculate(path.getDistance()));
    }

    private Sections getSections(List<SectionEntity> sectionEntities) {
        return new Sections(sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getId(),
                        stationDao.getStation(sectionEntity.getUpStationId()),
                        stationDao.getStation(sectionEntity.getDownStationId()),
                        sectionEntity.getDistance()))
                .collect(Collectors.toList()));
    }

    private void validateExists(Long id) {
        if (!stationDao.existById(id)) {
            throw new NoSuchElementException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }
}
