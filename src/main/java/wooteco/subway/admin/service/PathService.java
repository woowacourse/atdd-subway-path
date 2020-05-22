package wooteco.subway.admin.service;


import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.domain.exception.InvalidFindPathException;
import wooteco.subway.admin.domain.exception.NoExistStationException;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(String sourceName, String targetName, PathType type) {
        checkDuplicateName(sourceName, targetName);
        Lines lines = new Lines(lineRepository.findAll());
        Stations stations = new Stations(stationRepository.findAll());

        Long sourceId = findStationIdByStationName(sourceName);
        Long targetId = findStationIdByStationName(targetName);

        Path path = Path.makePath(lines, stations, type, sourceId, targetId);

        if (type.equals(PathType.DISTANCE)) {
            return new PathResponse(StationResponse.listOf(path.getPath().getStations()), path.getTotalInformation(), path.getTotalWeight());
        }

        return new PathResponse(StationResponse.listOf(path.getPath().getStations()), path.getTotalWeight(), path.getTotalInformation());
    }

    private void checkDuplicateName(String sourceName, String targetName) {
        if (sourceName.equals(targetName)) {
            throw new InvalidFindPathException(InvalidFindPathException.DUPLICATE_SOURCE_WITH_TARGET_ERROR_MSG);
        }
    }

    private Long findStationIdByStationName(String sourceName) {
        return stationRepository.findByName(sourceName)
                .orElseThrow(NoExistStationException::new)
                .getId();
    }
}
