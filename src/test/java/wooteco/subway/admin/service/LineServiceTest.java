package wooteco.subway.admin.service;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.repository.LineRepository;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    @Mock
    private LineRepository lineRepository;

    private LineService lineService;

    private Line line;

    @BeforeEach
    void setUp() {
        lineService = new LineService(lineRepository);

        line = new Line(1L, "2호선", "bg-green-500", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
    }

}
