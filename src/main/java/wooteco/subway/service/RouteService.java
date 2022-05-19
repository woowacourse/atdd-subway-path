package wooteco.subway.service;

import wooteco.subway.ui.dto.response.RouteResponse;

public interface RouteService {
    
    RouteResponse findRoute(Long sourceId, Long targetId, int age);
}
