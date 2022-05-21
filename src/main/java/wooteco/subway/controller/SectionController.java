package wooteco.subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.service.dto.request.SectionRequest;
import wooteco.subway.service.SectionService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/lines/{lineId}/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<Void> createSection(@PathVariable @NotNull Long lineId, @RequestBody @Valid SectionRequest sectionRequest) {
        sectionService.save(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSection(@PathVariable @NotNull Long lineId, @RequestParam @NotNull Long stationId) {
        sectionService.delete(lineId, stationId);
        return ResponseEntity.ok().build();
    }
}
