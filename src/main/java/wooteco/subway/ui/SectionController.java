package wooteco.subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.service.SectionService;
import wooteco.subway.service.dto.LineServiceResponse;
import wooteco.subway.service.dto.SectionServiceDeleteRequest;
import wooteco.subway.ui.dto.SectionRequest;

@RestController
@RequestMapping("/lines/{id}/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    public ResponseEntity<LineServiceResponse> createSection(
        @Validated @RequestBody SectionRequest sectionRequest, @PathVariable Long id) {
        Long savedId = sectionService.save(sectionRequest.toServiceRequest(), id);
        if (savedId != null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<LineServiceResponse> deleteSection(@PathVariable Long id,
        @RequestParam Long stationId) {
        if (sectionService.removeSection(new SectionServiceDeleteRequest(id, stationId))) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }
}
