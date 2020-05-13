package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineRequest;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.service.LineService;

@RestController
@RequestMapping("/api/lines")
public class LineController {
    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/api/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest view) {
        LineResponse line = lineService.save(view);

        return ResponseEntity
                .created(URI.create("/lines/" + line.getId()))
                .body(line);
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> showLine() {
        return ResponseEntity
            .ok()
            .body(lineService.showLines());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineDetailResponse> retrieveLine(@PathVariable Long id) {
        return ResponseEntity
            .ok()
            .body(lineService.findLineWithStationsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest view) {
        lineService.updateLine(id, view);
        return ResponseEntity
            .noContent()
            .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity
            .noContent()
            .build();
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> addLineStation(@PathVariable Long lineId, @RequestBody LineStationCreateRequest view) {
        lineService.addLineStation(lineId, view);
        return ResponseEntity
            .ok()
            .build();
    }

    @GetMapping("/stations")
    public ResponseEntity<List<LineDetailResponse>> showLineDetails() {
        final List<LineDetailResponse> wholeLineResponse = lineService.wholeLines();

        return ResponseEntity
            .ok()
            .body(wholeLineResponse);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.removeLineStation(lineId, stationId);
        return ResponseEntity
            .noContent()
            .build();
    }
}
