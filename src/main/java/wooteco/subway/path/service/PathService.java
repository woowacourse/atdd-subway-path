package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import wooteco.subway.path.domin.Path;
import wooteco.subway.path.domin.PathRepository;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
public class PathService {

    private final PathRepository pathRepository;
    private final StationService stationService;

    public PathService(final PathRepository pathRepository, final StationService stationService) {
        this.pathRepository = pathRepository;
        this.stationService = stationService;
    }


    public PathResponse getShortestDistancePath(Long source, Long target) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        Path path = pathRepository.generateShortestDistancePath(sourceStation, targetStation);

        return new PathResponse(StationResponse.listOf(path.getStations()), path.getDistance());
    }
}
