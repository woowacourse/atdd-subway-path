package wooteco.subway.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.admin.dto.*;
import wooteco.subway.admin.service.LineService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(
            @RequestBody LineRequest view
    ) {
        LineResponse lineResponse = lineService.createLine(view);
        return ResponseEntity
                .created(URI.create("/lines/" + lineResponse.getId()))
                .build();
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.showLines());
    }

    @GetMapping("/detail")
    public ResponseEntity<WholeSubwayResponse> showLinesWithDetail() {
        WholeSubwayResponse response = lineService.wholeLines();

        return ResponseEntity.ok()
                .eTag(String.valueOf(response.hashCode()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineDetailResponse> showLineWithDetail(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok().body(lineService.findLineWithStationsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(
            @PathVariable("id") Long id,
            @RequestBody LineRequest lineRequest
    ) {
        lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(
            @PathVariable("id") Long id
    ) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/stations")
    public ResponseEntity<Void> addLineStation(
            @PathVariable("id") Long id,
            @RequestBody LineStationCreateRequest lineStationRequest
    ) {
        lineService.addLineStation(id, lineStationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/stations/{stationId}")
    public ResponseEntity<Void> removeLineStation(
            @PathVariable Long id,
            @PathVariable Long stationId
    ) {
        lineService.removeLineStation(id, stationId);
        return ResponseEntity.noContent().build();
    }
}
