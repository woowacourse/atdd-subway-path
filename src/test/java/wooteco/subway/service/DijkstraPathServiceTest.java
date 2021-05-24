package wooteco.subway.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.web.dto.PathResponse;

@ExtendWith(MockitoExtension.class)
class DijkstraPathServiceTest {

    @Mock
    private LineDao lineDao;

    @InjectMocks
    private PathService pathService;

    private List<Line> lines;

    @BeforeEach
    void setUp() {
        Station 강남역 = new Station(1L, "강남역");
        Station 판교역 = new Station(2L, "판교역");
        Station 정자역 = new Station(3L, "정자역");
        Station 역삼역 = new Station(4L, "역삼역");
        Station 잠실역 = new Station(5L, "잠실역");

        Line 신분당선 = new Line(1L, "신분당선", "red lighten-1");
        신분당선.addSection(new Section(강남역, 판교역, 10));
        신분당선.addSection(new Section(판교역, 정자역, 10));

        Line 이호선 = new Line(2L, "2호선", "green lighten-1");
        이호선.addSection(new Section(강남역, 역삼역, 10));
        이호선.addSection(new Section(역삼역, 잠실역, 10));

        this.lines = Arrays.asList(신분당선, 이호선);
    }

    @Test
    void findShortestPath() {
        when(lineDao.findAll()).thenReturn(lines);
        final PathResponse pathResponse = pathService.findShortestPaths(1L, 3L);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
        assertThat(pathResponse.getStations()).hasSize(3);
        assertThat(pathResponse.getStations()).extracting("name")
            .containsExactlyInAnyOrder("강남역","판교역","정자역");
    }
}