package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.*;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.LineService;
import wooteco.subway.admin.service.PathService;

import java.net.URI;
import java.nio.file.Path;
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

    @GetMapping("/stations/shortest-path")
    public ResponseEntity<PathResponse> showShortestStationPath(@RequestParam String source, @RequestParam String target, @RequestParam String pathType) {
        PathResponse path = pathService.findPath(source, target, PathType.valueOf(pathType));
        return ResponseEntity.ok().body(path);
    }
}