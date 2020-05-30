package wooteco.subway.admin.controller.restcontroller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.request.StationCreateRequest;
import wooteco.subway.admin.dto.response.StationResponse;
import wooteco.subway.admin.service.StationService;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public ResponseEntity<List<StationResponse>> showStations() {
        return ResponseEntity.ok().body(stationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationResponse> showStation(@PathVariable Long id) {
        return ResponseEntity.ok(stationService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Long> createStation(@RequestBody StationCreateRequest view) {
        Long id = stationService.save(view);

        return ResponseEntity
            .created(URI.create("/stations/" + id))
            .body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
