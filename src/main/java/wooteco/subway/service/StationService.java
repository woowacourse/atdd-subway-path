package wooteco.subway.service;

import java.util.List;
import wooteco.subway.ui.dto.response.StationResponse;

public interface StationService {

    StationResponse create(String name);

    List<StationResponse> findAll();

    void deleteById(Long id);
}
