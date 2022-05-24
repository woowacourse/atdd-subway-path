package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.service.PathServiceRequest;
import wooteco.subway.dto.service.PathServiceResponse;
import wooteco.subway.dto.service.StationDto;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.strategy.DijkstraPathStrategy;

@Service
public class PathService {

    private static final String ERROR_MESSAGE_NOT_EXISTS_ID = "존재하지 않는 지하철 역입니다.";

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final DijkstraPathStrategy pathStrategy;

    public PathService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao,
                       DijkstraPathStrategy pathStrategy) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.pathStrategy = pathStrategy;
    }

    public PathServiceResponse getShortestPath(PathServiceRequest pathServiceRequest) {
        validateExists(pathServiceRequest.getSource());
        validateExists(pathServiceRequest.getTarget());
        validateNotSameStation(pathServiceRequest.getSource(), pathServiceRequest.getTarget());

        Station source = stationDao.getStation(pathServiceRequest.getSource());
        Station target = stationDao.getStation(pathServiceRequest.getTarget());
        Path path = pathStrategy.getPath(getAllLines(), source, target);

        return new PathServiceResponse(
                toStationDtos(path.getStations()),
                path.getDistance(),
                path.getFare(pathServiceRequest.getAge()));
    }

    private Lines getAllLines() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<SectionEntity> sectionEntities = sectionDao.findAll();

        List<Line> lines = new ArrayList<>();
        for (LineEntity lineEntity : lineEntities) {
            lines.add(new Line(
                    lineEntity.getId(),
                    lineEntity.getName(),
                    lineEntity.getColor(),
                    lineEntity.getExtraFare(),
                    getSectionsByLine(sectionEntities, lineEntity.getId())
            ));
        }

        return new Lines(lines);
    }

    private Sections getSectionsByLine(List<SectionEntity> sectionEntities, Long lineId) {
        return new Sections(sectionEntities.stream()
                .filter(sectionEntity -> sectionEntity.getLineId() == lineId)
                .map(sectionEntity -> new Section(
                        sectionEntity.getId(),
                        stationDao.getStation(sectionEntity.getUpStationId()),
                        stationDao.getStation(sectionEntity.getDownStationId()),
                        sectionEntity.getDistance()))
                .collect(Collectors.toList()));
    }

    private List<StationDto> toStationDtos(List<Station> stations) {
        List<StationDto> stationDtos = stations.stream()
                .map(station -> new StationDto(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return stationDtos;
    }

    private void validateExists(Long id) {
        if (!stationDao.existById(id)) {
            throw new NoSuchElementException(ERROR_MESSAGE_NOT_EXISTS_ID);
        }
    }

    private void validateNotSameStation(long source, long target) {
        if (source == target) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }
}
