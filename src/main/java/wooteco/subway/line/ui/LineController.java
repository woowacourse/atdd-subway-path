package wooteco.subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.application.dto.LineRequestDto;
import wooteco.subway.line.application.dto.LineResponseDto;
import wooteco.subway.line.application.dto.SectionRequestDto;
import wooteco.subway.line.ui.dto.LineRequest;
import wooteco.subway.line.ui.dto.LineResponse;
import wooteco.subway.line.ui.dto.SectionRequest;
import wooteco.subway.line.ui.dto.valid.NumberValidation;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Validated
@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity<LineResponse> createLine(@Valid @RequestBody LineRequest lineRequest) {
        LineResponseDto lineResponseDto = lineService.saveLine(new LineRequestDto(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()
        ));

        return ResponseEntity
                .created(URI.create("/lines/" + lineResponseDto.getId()))
                .body(LineResponse.of(lineResponseDto));
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines() {
        List<LineResponse> lineResponses = getLineResponses();

        return ResponseEntity.ok(lineResponses);
    }

    private List<LineResponse> getLineResponses() {
        return lineService.findLineResponses().stream()
                .map(LineResponse::of)
                .collect(toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@NumberValidation @PathVariable Long id) {
        LineResponseDto lineResponseDto = lineService.findLineResponseById(id);

        return ResponseEntity.ok(LineResponse.of(lineResponseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLine(@NumberValidation @PathVariable Long id,
                                           @Valid @RequestBody LineRequest lineRequest) {
        lineService.updateLine(id, new LineRequestDto(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()
        ));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(@NumberValidation @PathVariable Long id) {
        lineService.deleteLineById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> addLineStation(@NumberValidation @PathVariable Long lineId,
                                               @Valid @RequestBody SectionRequest sectionRequest) {
        lineService.addLineStation(lineId, new SectionRequestDto(
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(),
                sectionRequest.getDistance())
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> removeLineStation(@NumberValidation @PathVariable Long lineId,
                                                  @NumberValidation @RequestParam Long stationId) {
        lineService.removeLineStation(lineId, stationId);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Void> handleSQLException() {
        return ResponseEntity.badRequest().build();
    }

}
