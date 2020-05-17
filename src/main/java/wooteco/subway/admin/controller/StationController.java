package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.StationRepository;
import wooteco.subway.admin.service.StationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping()
    public ResponseEntity<Void> createStation(@RequestBody StationCreateRequest view) {
        StationResponse stationResponse = stationService.createStation(view);

        return ResponseEntity
            .created(URI.create("/api/stations/" + stationResponse.getId()))
            .build();
    }

    @GetMapping()
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationResponse> stationResponses = stationService.showStations();

        return ResponseEntity
            .ok()
            .body(stationResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStation(@PathVariable Long id) {
        stationService.removeStation(id);

        return ResponseEntity
            .noContent()
            .build();
    }
}
