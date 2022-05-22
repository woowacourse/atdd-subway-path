package wooteco.subway.helper;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;

public class LineAddAndRequest extends Request {

    private final Long id;
    private final TLine tLine;
    private final SectionRequest sectionRequest;

    public LineAddAndRequest(Long id, TLine tLine, SectionRequest sectionRequest) {
        this.id = id;
        this.tLine = tLine;
        this.sectionRequest = sectionRequest;
    }

    public LineAddAndRequest(TLine tLine, SectionRequest sectionRequest) {
        this(tLine.노선을등록한다(sectionRequest).getId(), tLine, sectionRequest);
    }

    public void 중복노선을등록한다(int status) {
        tLine.노선을등록한다(sectionRequest, status);
    }

    public List<LineResponse> 전체노선을조회한다(int status) {
        ExtractableResponse<Response> response = get("/lines");

        assertThat(response.statusCode()).isEqualTo(status);
        return response.jsonPath().getList(".", LineResponse.class);
    }

    public LineResponse 단건노선을조회한다(int status) {
        ExtractableResponse<Response> response = 단건노선을조회한다();

        assertThat(response.statusCode()).isEqualTo(status);
        return response.as(LineResponse.class);
    }

    public ExtractableResponse<Response> 단건노선을조회한다() {
        return get(String.format("/lines/%d", id));
    }

    public void 정보를변경한다(TLine tLine, int status) {
        LineRequest lineRequest = createLineRequest(tLine);
        ExtractableResponse<Response> response = put(lineRequest, String.format("/lines/%d", id));

        assertThat(response.statusCode()).isEqualTo(status);
    }

    public void 노선을제거한다(int status) {
        ExtractableResponse<Response> response = delete(String.format("/lines/%d", id));

        assertThat(response.statusCode()).isEqualTo(status);
    }

    public List<ExtractableResponse<Response>> 구간을등록한다(SectionRequest... sectionRequests) {
        return List.of(sectionRequests).stream()
                .map(this::구간을등록한다)
                .collect(Collectors.toList());
    }

    public ExtractableResponse<Response> 구간을등록한다(SectionRequest sectionRequest) {
        return post(sectionRequest, String.format("/lines/%d/sections", id));
    }

    public void 구간을삭제한다(Station station, int status) {
        ExtractableResponse<Response> response = delete(String.format("/lines/%d/sections?stationId=%d", id, station.getId()));

        assertThat(response.statusCode()).isEqualTo(status);
    }

    private LineRequest createLineRequest(TLine tLine) {
        return new LineRequest(
                tLine.getName(),
                tLine.getColor(),
                tLine.getExtraFare(),
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(),
                sectionRequest.getDistance()
        );
    }
}
