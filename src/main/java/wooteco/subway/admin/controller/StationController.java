package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.service.StationService;

@RequestMapping("/stations")
@RestController
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(
            @RequestBody @Valid StationCreateRequest stationCreateRequest) {
        StationResponse stationResponse = stationService.createStation(stationCreateRequest);
        return ResponseEntity
                .created(URI.create("/stations/" + stationResponse.getId()))
                .body(stationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> findStationById(@PathVariable Long id) {
        StationResponse stationResponse = stationService.findStationById(id);
        return ResponseEntity
                .ok(stationResponse);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> findStations() {
        List<StationResponse> stationResponses = stationService.findAll();
        return ResponseEntity.ok()
                .body(stationResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
