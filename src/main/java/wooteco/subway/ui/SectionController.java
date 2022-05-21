package wooteco.subway.ui;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.service.SectionService;

@Controller
@Validated
public class SectionController {

    private static final String NUMBER_MIN_RANGE_ERROR = " 1 이상이여야 합니다.";

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> createSection(@PathVariable @Min(value = 1, message = "라인 아이디는" + NUMBER_MIN_RANGE_ERROR)
                                                  final Long lineId,
                                              @RequestBody @Valid final SectionRequest request) {
        sectionService.saveSection(lineId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSection(@PathVariable @Min(value = 1, message = "라인 아이디는" + NUMBER_MIN_RANGE_ERROR)
                                                  final Long lineId,
                                              @RequestParam @Min(value = 1, message = "정거장 아이디는" + NUMBER_MIN_RANGE_ERROR)
                                              final Long stationId) {
        sectionService.deleteSection(lineId, stationId);
        return ResponseEntity.ok().build();
    }
}
