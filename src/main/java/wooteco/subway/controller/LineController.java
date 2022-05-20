package wooteco.subway.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.service.LineService;
import wooteco.subway.service.SectionService;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final SectionService sectionService;

    public LineController(final LineService lineService, final SectionService sectionService) {
        this.lineService = lineService;
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@RequestBody @Valid final LineRequest lineRequest) {
        final Line line = lineRequest.toEntity();

        final Line newLine = lineService.createLine(line, lineRequest.toSectionEntity());
        final List<Station> stations = findStationsByLine(newLine);
        final LineResponse lineResponse = LineResponse.from(newLine, stations);

        return ResponseEntity.created(URI.create("/lines/" + newLine.getId())).body(lineResponse);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findLines() {
        final List<Line> lines = lineService.findAllLines();
        final List<LineResponse> lineResponses = lines.stream()
                .map(line -> LineResponse.from(line, findStationsByLine(line)))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(lineResponses);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LineResponse> findLine(@PathVariable final Long id) {
        final Line line = lineService.findLineById(id);
        final LineResponse lineResponse = LineResponse.from(line, findStationsByLine(line));

        return ResponseEntity.ok().body(lineResponse);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateLine(@PathVariable final Long id,
                                           @RequestBody @Valid final LineRequest lineRequest) {
        final Line line = lineRequest.toEntity();
        lineService.update(id, line);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable final Long id) {
        lineService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sections")
    public void addSection(@PathVariable final Long id, @RequestBody @Valid final SectionRequest sectionRequest) {
        sectionService.addSection(id, SectionRequest.toEntity(sectionRequest));
    }

    @DeleteMapping("/{id}/sections")
    public void deleteSection(@PathVariable final Long id, @RequestParam final Long stationId) {
        sectionService.delete(id, stationId);
    }

    private List<Station> findStationsByLine(final Line line) {
        return sectionService.findStationsByLine(line.getId());
    }
}
