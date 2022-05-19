package wooteco.subway.ui;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.subway.application.SectionService;
import wooteco.subway.domain.Section;
import wooteco.subway.dto.request.SectionRequest;

@RequestMapping("/lines/{lineId}/sections")
@RestController
@AllArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createSection(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        Section section = sectionRequest.toSection(lineId);
        sectionService.createSection(section);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteSections(@PathVariable Long lineId, @RequestParam Long stationId) {
        sectionService.removeSection(lineId, stationId);
    }
}
