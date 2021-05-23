package wooteco.subway.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public class PathRequest {
    @NotNull
    private final long source;
    @NotNull
    private final long target;
}
