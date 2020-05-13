package wooteco.subway.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PathCriteria {
    DISTANCE,
    DURATION
}
