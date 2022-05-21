package wooteco.subway.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.StationServiceResponse;
import wooteco.subway.ui.dto.StationRequest;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<StationServiceResponse> createStation(
            @Validated @RequestBody StationRequest stationRequest) {
        StationServiceResponse stationServiceResponse = stationService.save(
                stationRequest.toServiceRequest());
        return ResponseEntity.created(URI.create("/stations/" + stationServiceResponse.getId()))
                .body(
                        stationServiceResponse);
    }

    @GetMapping
    public ResponseEntity<List<StationServiceResponse>> showStations() {
        List<StationServiceResponse> stations = stationService.findAll();
        return ResponseEntity.ok().body(stations);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        if (stationService.deleteById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }
}
