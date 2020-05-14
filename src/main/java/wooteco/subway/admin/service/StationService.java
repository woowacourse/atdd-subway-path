package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathRequestWithId;
import wooteco.subway.admin.dto.PathType;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotFoundInputValueException;
import wooteco.subway.admin.exception.NotFoundPathException;
import wooteco.subway.admin.exception.NotFoundStationException;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse save(StationCreateRequest request) {
        Station persistStation = stationRepository.save(request.toStation());

        return StationResponse.of(persistStation);
    }

    public List<StationResponse> findAll() {
        List<Station> stations = stationRepository.findAll();

        return StationResponse.listOf(stations);
    }

    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }

    public PathRequestWithId toPathRequestWithId(PathRequest pathRequest) {
        validatePathRequest(pathRequest);
        String sourceName = pathRequest.getSourceName();
        String targetName = pathRequest.getTargetName();
        PathType pathType = PathType.of(pathRequest.getType());

        return new PathRequestWithId(findStationIdByName(sourceName), findStationIdByName(targetName), pathType);
    }

    private void validatePathRequest(PathRequest pathRequest) {
        if ("".equals(pathRequest.getSourceName())) {
            throw new NotFoundInputValueException("출발역");
        }
        if ("".equals(pathRequest.getTargetName())) {
            throw new NotFoundInputValueException("도착역");
        }

        if (pathRequest.getSourceName().equals(pathRequest.getTargetName())) {
            throw new NotFoundPathException();
        }
    }

    private Long findStationIdByName(String sourceName) {
        return stationRepository.findByName(sourceName).map(Station::getId)
                .orElseThrow(NotFoundStationException::new);
    }

    public List<Station> findAllById(List<Long> lineStationsId) {
        return stationRepository.findAllById(lineStationsId);
    }
}
