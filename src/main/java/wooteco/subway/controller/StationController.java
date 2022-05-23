package wooteco.subway.controller;

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

import wooteco.subway.controller.dto.StationRequest;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.StationResponse;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationResponse> createStation(@RequestBody @Valid StationRequest stationRequest) {
        StationResponse stationResponse = stationService.create(stationRequest.getName());
        return ResponseEntity.created(URI.create("/stations/" + stationResponse.getId()))
            .body(stationResponse);
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        List<StationResponse> stationResponses = stationService.findAllStations();
        return ResponseEntity.ok().body(stationResponses);
    }

    @DeleteMapping("/{stationId}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long stationId) {
        stationService.remove(stationId);
        return ResponseEntity.noContent().build();
    }
}
