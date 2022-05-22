package wooteco.subway.service;

import wooteco.subway.service.dto.request.PathServiceRequest;
import wooteco.subway.service.dto.response.PathServiceResponse;

public interface PathService {

    PathServiceResponse findPath(PathServiceRequest pathServiceRequest);
}
