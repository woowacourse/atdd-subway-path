package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.StationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/stations")
public class StationController {
    private final StationService stationService;

    public StationController(final StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createStation(
            @RequestBody StationCreateRequest stationRequest
    ) {
        StationResponse stationResponse = stationService.createStation(stationRequest);
        return ResponseEntity
                .created(URI.create("/stations/" + stationResponse.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.showStations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(
            @PathVariable Long id
    ) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }
}
