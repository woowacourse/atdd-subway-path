package wooteco.subway.admin.acceptance.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;

public class LineAcceptanceTest extends AcceptanceTest {

    public void createLine(String name) {
        String path = "/api/lines";
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");
        params.put("backgroundColor", "bg-gray-300");

        super.post(params, path);
    }

    public LineDetailResponse getLine(Long id) {
        String path = "/api/lines/" + id;
        return super.get(path, LineDetailResponse.class);
    }

    protected void deleteLine(Long id) {
        String path = "/api/lines/" + id;

        super.delete(path);
    }

    protected void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
        Map<String, String> params = new HashMap<>();
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        String path = "/api/lines/" + id;
        super.put(path, params);
    }

    protected List<LineResponse> getLines() {
        String path = "/api/lines";

        return super.getList(path, LineResponse.class);
    }

    public List<LineDetailResponse> getLineDetails() {
        String path = "/api/lines/detail";

        return super.getList(path, LineDetailResponse.class);
    }
}
