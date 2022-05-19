package wooteco.subway.service;

import java.util.List;
import wooteco.subway.ui.dto.request.LineRequest;
import wooteco.subway.ui.dto.request.LineUpdateRequest;
import wooteco.subway.ui.dto.request.SectionRequest;
import wooteco.subway.ui.dto.response.LineResponse;

public interface LineService {

    LineResponse create(LineRequest lineRequest);

    List<LineResponse> findAll();

    LineResponse findById(Long id);

    void updateById(Long id, LineUpdateRequest lineUpdateRequest);

    void deleteById(Long id);

    void addSection(Long lineId, SectionRequest sectionRequest);

    void removeSection(Long lineId, Long stationId);
}
