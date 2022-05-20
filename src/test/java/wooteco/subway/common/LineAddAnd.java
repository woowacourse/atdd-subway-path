package wooteco.subway.common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.dto.SectionRequest;

public class LineAddAnd extends Request {

    private Long id;
    private TLine tLine;

    public LineAddAnd(TLine tLine, Long startStationId, Long endStationId, int distance) {
        this.id = tLine.노선을등록한다(startStationId, endStationId, distance).getId();
        this.tLine = tLine;
    }

    public List<ExtractableResponse<Response>> 구간을등록한다(SectionRequest ... sectionRequests) {
        return List.of(sectionRequests).stream()
                .map(this::구간을등록한다)
                .collect(Collectors.toList());
    }

    public ExtractableResponse<Response> 구간을등록한다(SectionRequest sectionRequest) {
        return post(sectionRequest, String.format("/lines/%d/sections", id));
    }

}
