package wooteco.subway.ui;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.application.LineService;
import wooteco.subway.application.SectionService;
import wooteco.subway.application.StationService;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@RequestMapping("/lines")
@RestController
@AllArgsConstructor
public class LineController {

    private final LineService lineService;

    private final SectionService sectionService;

    private final StationService stationService;

    @PostMapping
    public ResponseEntity<LineResponse> createLines(@RequestBody LineRequest lineRequest) {
        Line savedLine = lineService.saveAndGet(lineRequest.toLine(), lineRequest.toSection());
        LinkedList<Long> sortedStationIds = sectionService.findSortedStationIds(savedLine.getId());
        List<Station> stations = stationService.findByIdIn(sortedStationIds);

        LineResponse lineResponse = new LineResponse(savedLine, stations);
        return ResponseEntity.created(URI.create("/lines/" + lineResponse.getId())).body(lineResponse);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LineResponse> showLines() {
        List<Line> lines = lineService.findAll();
        List<LineResponse> lineResponses = new ArrayList<>();

        for (Line line : lines) {
            LinkedList<Long> sortedStationIds = sectionService.findSortedStationIds(line.getId());
            List<Station> stations = stationService.findByIdIn(sortedStationIds);
            LineResponse lineResponse = new LineResponse(line, stations);
            lineResponses.add(lineResponse);
        }

        return lineResponses;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LineResponse showLine(@PathVariable Long id) {
        Line line = lineService.findById(id);
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LineResponse updateLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) {
        Line line = lineService.update(id, lineRequest.getName(), lineRequest.getColor());
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLine(@PathVariable Long id) {
        lineService.deleteById(id);
    }
}
