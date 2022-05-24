package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.TestFixtures.LINE_SIX;
import static wooteco.subway.TestFixtures.LINE_SIX_COLOR;
import static wooteco.subway.TestFixtures.LINE_TWO;
import static wooteco.subway.TestFixtures.LINE_TWO_COLOR;
import static wooteco.subway.TestFixtures.LINE_분당;
import static wooteco.subway.TestFixtures.LINE_분당_COLOR;
import static wooteco.subway.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.TestFixtures.STANDARD_FARE;
import static wooteco.subway.TestFixtures.동묘앞역;
import static wooteco.subway.TestFixtures.선릉역;
import static wooteco.subway.TestFixtures.신당역;
import static wooteco.subway.TestFixtures.창신역;
import static wooteco.subway.TestFixtures.한티역;

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
class PathServiceTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private PathService pathService;


    @DisplayName("구간 사이의 최소거리, 역, 비용을 반환한다")
    @Test
    void calculateMinDistance() {
        Station saved_신당역 = createStation(신당역);
        Station saved_동묘앞역 = createStation(동묘앞역);
        Station saved_창신역 = createStation(창신역);

        Line line = createLine(LINE_SIX, LINE_SIX_COLOR);

        sectionRepository.save(new Section(line.getId(), saved_신당역, saved_동묘앞역, STANDARD_DISTANCE));
        sectionRepository.save(new Section(line.getId(), saved_동묘앞역, saved_창신역, STANDARD_DISTANCE));

        PathResponse pathResponse = pathService.calculateShortestPath(
                new PathRequest(saved_신당역.getId(), saved_창신역.getId(), 10));
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

    @DisplayName("경로에 추가 요금이 있는 경우, 가장 높은 금액의 추가 요금만 더한다.")
    @Test
    void calculateExtraFare() {
        Station saved_한티역 = createStation(한티역);
        Station saved_선릉역 = createStation(선릉역);
        Station saved_신당역 = createStation(신당역);
        Station saved_동묘앞역 = createStation(동묘앞역);

        Line line1 = createLine(LINE_SIX, LINE_SIX_COLOR);
        sectionRepository.save(new Section(line1.getId(), saved_신당역, saved_동묘앞역, 1));
        Long lineId2 = lineRepository.save(new Line(LINE_TWO, LINE_TWO_COLOR, 500));
        sectionRepository.save(new Section(lineId2, saved_선릉역, saved_신당역, 1));
        Long lineId3 = lineRepository.save(new Line(LINE_분당, LINE_분당_COLOR, 400));
        sectionRepository.save(new Section(lineId3, saved_한티역, saved_선릉역, 1));

        PathResponse response = pathService.calculateShortestPath(
                new PathRequest(saved_한티역.getId(), saved_동묘앞역.getId(), 20));

        assertThat(response.getFare()).isEqualTo(STANDARD_FARE + 500);
    }

    private Line createLine(String lineName, String lineColor) {
        Long id = lineRepository.save(new Line(lineName, lineColor));
        return new Line(id, lineName, lineColor);
    }

    private Station createStation(Station station) {
        return stationRepository.save(station);
    }
}
