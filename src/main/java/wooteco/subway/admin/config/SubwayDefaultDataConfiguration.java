package wooteco.subway.admin.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.time.LocalTime;

@Configuration
public class SubwayDefaultDataConfiguration {

    @Profile("local")
    @Configuration
    private class LocalDataApplicationRunner implements ApplicationRunner {

        private final LineRepository lineRepository;
        private final StationRepository stationRepository;

        public LocalDataApplicationRunner(final LineRepository lineRepository, final StationRepository stationRepository) {
            this.lineRepository = lineRepository;
            this.stationRepository = stationRepository;
        }

        @Override
        public void run(final ApplicationArguments args) throws Exception {
            Station station1 = stationRepository.save(new Station("터틀"));
            Station station2 = stationRepository.save(new Station("비밥"));
            Station station3 = stationRepository.save(new Station("포비"));

            Line lineNumber1 = new Line("1호선", LocalTime.of(05, 10), LocalTime.of(22, 10), 10);
            lineNumber1.addEdge(new Edge(null, station1.getId(), 10, 10));
            lineNumber1.addEdge(new Edge(station1.getId(), station2.getId(), 1, 10));
            lineNumber1.addEdge(new Edge(station2.getId(), station3.getId(), 10, 10));
            Line lineNumber2 = new Line("2호선", LocalTime.of(05, 20), LocalTime.of(23, 30), 15);
            lineNumber2.addEdge(new Edge(null, station2.getId(), 10, 10));
            lineNumber2.addEdge(new Edge(station2.getId(), station3.getId(), 1, 10));

            lineNumber1 = lineRepository.save(lineNumber1);
            lineNumber2 = lineRepository.save(lineNumber2);
        }
    }

}
