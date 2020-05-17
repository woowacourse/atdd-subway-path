package wooteco.subway.admin.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.admin.domain.Line;
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

    @PostMapping()
    public ResponseEntity<Void> createLine(@RequestBody @Valid LineRequest lineRequest) {
        LineResponse lineResponse = lineService.addLine(lineRequest);

        return ResponseEntity
            .created(URI.create("/api/lines/" + lineResponse.getId()))
            .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineDetailResponse> findLine(@PathVariable Long id) {
        return ResponseEntity
            .ok()
            .body(lineService.findLineWithStationsById(id));
    }

    @GetMapping()
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity
            .ok()
            .body(lineService.showLines());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, lineRequest);

        return ResponseEntity
            .noContent()
            .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        
        return ResponseEntity
            .noContent()
            .build();
    }

    @PostMapping("/{lineId}/stations")
    public ResponseEntity<Void> createLineStation(@PathVariable Long lineId,
        @RequestBody LineStationCreateRequest lineStationCreateRequest) {
        LineResponse lineResponse = lineService.addLineStation(lineId, lineStationCreateRequest);

        return ResponseEntity
            .created(URI.create("/api/lines/" + lineResponse.getId()))
            .build();
    }

    @GetMapping("/detail")
    public ResponseEntity<List<LineDetailResponse>> showLineDetails() {
        final List<LineDetailResponse> lineDetailResponses = lineService.wholeLines();

        return ResponseEntity
            .ok()
            .body(lineDetailResponses);
    }

    @DeleteMapping("/{lineId}/stations/{stationId}")
    public ResponseEntity<Void> removeLineStation(@PathVariable Long lineId, @PathVariable Long stationId) {
        lineService.removeLineStation(lineId, stationId);

        return ResponseEntity
            .noContent()
            .build();
    }
}
