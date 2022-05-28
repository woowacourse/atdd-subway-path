package wooteco.subway.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import wooteco.subway.controller.dto.request.LineRequestForm;
import wooteco.subway.controller.dto.request.LineUpdateRequestForm;
import wooteco.subway.controller.dto.request.RequestFormAssembler;
import wooteco.subway.controller.dto.request.SectionRequestForm;
import wooteco.subway.controller.dto.response.LineResponseForm;
import wooteco.subway.controller.dto.response.ResponseFormAssembler;
import wooteco.subway.exception.DuplicateLineColorException;
import wooteco.subway.exception.DuplicateLineNameException;
import wooteco.subway.service.LineService;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.request.LineRequest;
import wooteco.subway.service.dto.response.LineResponse;
import wooteco.subway.service.dto.response.StationResponse;

@RestController
@RequestMapping("/lines")
public class LineController {

    private final LineService lineService;
    private final StationService stationService;
    private final RequestFormAssembler requestFormAssembler;
    private final ResponseFormAssembler responseFormAssembler;

    public LineController(LineService lineService, StationService stationService,
                          RequestFormAssembler requestFormAssembler, ResponseFormAssembler responseFormAssembler) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.requestFormAssembler = requestFormAssembler;
        this.responseFormAssembler = responseFormAssembler;
    }

    @PostMapping
    public ResponseEntity<LineResponseForm> createLine(@RequestBody LineRequestForm lineRequestForm) {
        LineRequest lineRequest = requestFormAssembler.lineRequest(lineRequestForm);
        LineResponse lineResponse = lineService.create(lineRequest);
        LineResponseForm lineResponseForm = getLineResponseForm(lineResponse);
        URI redirectUri = URI.create("/lines/" + lineResponseForm.getId());
        return ResponseEntity.created(redirectUri).body(lineResponseForm);
    }

    @GetMapping
    public ResponseEntity<List<LineResponseForm>> showLines() {
        List<LineResponseForm> lineResponses = getLineResponseForms(lineService.findAll());
        return ResponseEntity.ok(lineResponses);
    }

    @GetMapping("/{lineId}")
    public ResponseEntity<LineResponseForm> showLine(@PathVariable Long lineId) {
        LineResponseForm lineResponse = getLineResponseForm(lineService.findById(lineId));
        return ResponseEntity.ok(lineResponse);
    }

    @PutMapping(value = "/{lineId}")
    public ResponseEntity<Void> updateLine(@PathVariable Long lineId,
                                           @RequestBody LineUpdateRequestForm lineUpdateRequestForm) {
        lineService.update(lineId, requestFormAssembler.lineRequest(lineUpdateRequestForm));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long lineId) {
        lineService.delete(lineId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity<Void> appendSection(@PathVariable Long lineId,
                                              @RequestBody SectionRequestForm sectionRequestForm) {
        lineService.appendSection(lineId, requestFormAssembler.sectionRequest(sectionRequestForm));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity<Void> removeStation(@PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.removeStation(lineId, stationId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({DuplicateLineNameException.class, DuplicateLineColorException.class})
    public ResponseEntity<String> handle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    private List<LineResponseForm> getLineResponseForms(List<LineResponse> lineResponses) {
        return lineResponses.stream()
                .map(this::getLineResponseForm)
                .collect(Collectors.toUnmodifiableList());
    }

    private LineResponseForm getLineResponseForm(LineResponse lineResponse) {
        List<StationResponse> stationResponses = lineResponse.getSectionResponses()
                .stream()
                .flatMap(sectionResponse -> Stream.of(
                        sectionResponse.getUpStationId(),
                        sectionResponse.getDownStationId()))
                .distinct()
                .map(stationService::findById)
                .collect(Collectors.toUnmodifiableList());
        return responseFormAssembler.lineResponseForm(lineResponse, stationResponses);
    }
}
