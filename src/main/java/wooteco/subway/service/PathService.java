package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.element.Station;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.SubwayGraph;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.request.PathsRequest;
import wooteco.subway.service.dto.response.PathResponse;
import wooteco.subway.service.dto.response.StationResponse;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository,
                       StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse showPaths(PathsRequest pathsRequest) {
        return toPathResponse(Path.create(
                new SubwayGraph(sectionRepository.findAll()),
                stationRepository.findById(pathsRequest.getSource()),
                stationRepository.findById(pathsRequest.getTarget()),
                pathsRequest.getAge()
        ));
    }

    private PathResponse toPathResponse(Path path) {
        return new PathResponse(
                toStationResponse(path.getStations()),
                path.getDistance(),
                path.getFare());
    }

    private List<StationResponse> toStationResponse(List<Station> route) {
        return route.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }
}
