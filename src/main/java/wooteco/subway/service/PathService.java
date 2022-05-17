package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.exception.IllegalInputException;

@Service
public class PathService {

    public void find(final Long sourceStationId, final Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalInputException("출발역과 도착역이 동일합니다.");
        }
    }
}
