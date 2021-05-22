package wooteco.subway.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PathRequest {
    private final long source;
    private final long target;
}
