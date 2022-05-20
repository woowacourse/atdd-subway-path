package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.common.TestFixtures.LINE_COLOR;
import static wooteco.subway.common.TestFixtures.LINE_SIX_NAME;
import static wooteco.subway.common.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.common.TestFixtures.동묘앞역;
import static wooteco.subway.common.TestFixtures.신당역;
import static wooteco.subway.common.TestFixtures.창신역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Transactional
@SpringBootTest
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @DisplayName("구간 사이의 최소거리, 역, 비용을 반환한다")
    @Test
    void calculateMinDistance() {
        Station saved_신당역 = createStation(신당역);
        Station saved_동묘앞역 = createStation(동묘앞역);
        Station saved_창신역 = createStation(창신역);

        Line line = createLine(LINE_SIX_NAME, LINE_COLOR, 0);

        sectionRepository.save(new Section(line.getId(), saved_신당역, saved_동묘앞역, STANDARD_DISTANCE));
        sectionRepository.save(new Section(line.getId(), saved_동묘앞역, saved_창신역, STANDARD_DISTANCE));

        PathResponse pathResponse = pathService.calculateMinDistance(new PathRequest(saved_신당역.getId(), saved_창신역.getId(), 10));
        List<StationResponse> stations = pathResponse.getStations();

        assertAll(() -> {
            assertThat(stations).hasSize(3);
            assertThat(stations).containsExactly(new StationResponse(saved_신당역),
                    new StationResponse(saved_동묘앞역),
                    new StationResponse(saved_창신역));
            assertThat(pathResponse.getDistance()).isEqualTo(STANDARD_DISTANCE + STANDARD_DISTANCE);
            assertThat(pathResponse.getFare()).isEqualTo(1450);
        });

    }

    private Line createLine(String lineName, String lineColor, int extraFare) {
        Long id = lineRepository.save(new Line(lineName, lineColor, extraFare));
        return new Line(id, lineName, lineColor);
    }

    private Station createStation(Station station) {
        return stationRepository.save(station);
    }

}
