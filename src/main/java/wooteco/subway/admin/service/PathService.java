package wooteco.subway.admin.service;


import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.*;
import wooteco.subway.admin.domain.path.Path;
import wooteco.subway.admin.domain.path.PathMaker;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.OverlappedStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse calculatePath(String source, String target, PathSearchType type) {
        validateOverlappedStation(source, target);

        Lines allLines = new Lines(lineRepository.findAll());
        LineStations lineStations = new LineStations(allLines.getAllLineStation());
        Stations stations = new Stations(stationRepository.findAll());

        Station sourceStation = stations.findStationByName(source);
        Station targetStation = stations.findStationByName(target);

        PathMaker pathMaker = new PathMaker(lineStations, stations);
        Path path = pathMaker.computeShortestPath(sourceStation.getId(), targetStation.getId(), type);

        return PathResponse.of(path);
    }

    private void validateOverlappedStation(String source, String target) {
        if (source.equals(target)) {
            throw new OverlappedStationException();
        }
    }
}
