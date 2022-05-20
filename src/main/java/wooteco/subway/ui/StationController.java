package wooteco.subway.ui;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.application.StationService;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/stations")
@RestController
@AllArgsConstructor
public class StationController {

    private final StationService stationService;

    @PostMapping
    public ResponseEntity<StationResponse> createStation(
            @RequestBody StationRequest stationRequest) {
        Station station = stationService.saveAndGet(stationRequest.getName());
        StationResponse stationResponse = new StationResponse(station);
        return ResponseEntity.created(URI.create("/stations/" + station.getId()))
                .body(stationResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StationResponse> showStations() {
        return toStationResponses();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStation(@PathVariable Long id) {
        stationService.deleteById(id);
    }

    private List<StationResponse> toStationResponses() {
        return stationService.findAll().stream()
                .map(station -> new StationResponse(station))
                .collect(Collectors.toList());
    }
}
