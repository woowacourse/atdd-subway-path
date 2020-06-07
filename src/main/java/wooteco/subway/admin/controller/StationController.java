package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.PathService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class StationController {
    private final PathService pathService;

    private final StationRepository stationRepository;

    public StationController(PathService pathService, StationRepository stationRepository) {
        this.pathService = pathService;
        this.stationRepository = stationRepository;
    }

    @PostMapping("/stations")
    public ResponseEntity<StationResponse> createStation(@RequestBody StationCreateRequest view) {
        Station station = view.toStation();
        Station persistStation = stationRepository.save(station);

        return ResponseEntity
                .created(URI.create("/stations/" + persistStation.getId()))
                .body(StationResponse.of(persistStation));
    }

    @GetMapping("/stations")
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(StationResponse.listOf(stationRepository.findAll()));
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity deleteStation(@PathVariable Long id) {
        stationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stations/path")
    public ResponseEntity<PathResponse> showShortestStationPath(@ModelAttribute @Valid PathRequest pathRequest) {
        PathType pathType = PathType.valueOf(pathRequest.getPathType());
        PathResponse path = pathService.findPath(pathRequest.getSource(), pathRequest.getTarget(), pathType);
        return ResponseEntity.ok().body(path);
    }
}