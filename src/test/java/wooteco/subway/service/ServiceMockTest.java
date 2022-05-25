package wooteco.subway.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class ServiceMockTest {

    @Mock
    protected StationRepository stationRepository;

    @Mock
    protected SectionRepository sectionRepository;

    @Mock
    protected LineRepository lineRepository;

    @Mock
    protected StationService stationService;

    @Mock
    protected SectionService sectionService;
}
