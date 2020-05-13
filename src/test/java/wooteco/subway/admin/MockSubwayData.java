package wooteco.subway.admin;

import org.springframework.context.annotation.Configuration;

import wooteco.subway.admin.dto.StationCreateRequest;
import wooteco.subway.admin.service.LineService;
import wooteco.subway.admin.service.StationService;

@Configuration
public class MockSubwayData {
    private final StationService stationService;
    private final LineService lineService;

    public MockSubwayData(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public void initMockSubwayData() {
        stationService.createStation(new StationCreateRequest("빙봉역"));
        stationService.createStation(new StationCreateRequest("럿고역"));
        stationService.createStation(new StationCreateRequest(""));
    }
}
