package wooteco.subway.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.controller.dto.LineRequest;
import wooteco.subway.controller.dto.LineResponse;
import wooteco.subway.controller.dto.SectionRequest;
import wooteco.subway.domain.Line;
import wooteco.subway.service.LineService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        Line line = lineService.create(lineRequest.getName(), lineRequest.getColor(), lineRequest.toSectionRequest());
        return ResponseEntity.created(URI.create("/lines/" + line.getId()))
            .body(LineResponse.from(line));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> showLines() {
        List<LineResponse> lineResponses = lineService.listLines().stream()
            .map(LineResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long lineId) {
        Line line = lineService.findOne(lineId);
        return ResponseEntity.ok(LineResponse.from(line));
    }

    @PutMapping("/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId, @RequestBody LineRequest lineRequest) {
        lineService.update(lineRequest.toEntity(lineId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.remove(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addSection(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.deleteSection(lineId, stationId);
        return ResponseEntity.ok().build();
    }
}
