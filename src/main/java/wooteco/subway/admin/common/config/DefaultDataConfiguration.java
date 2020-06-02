package wooteco.subway.admin.common.config;

import java.time.LocalTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import wooteco.subway.admin.line.domain.line.Line;
import wooteco.subway.admin.line.domain.lineStation.LineStation;
import wooteco.subway.admin.line.repository.line.LineRepository;
import wooteco.subway.admin.station.domain.Station;
import wooteco.subway.admin.station.repository.StationRepository;

@Configuration
public class DefaultDataConfiguration {

    @Profile("local")
    @Configuration
    private static class LocalDefaultDataConfiguration implements ApplicationRunner {

        private final LineRepository lineRepository;
        private final StationRepository stationRepository;

        public LocalDefaultDataConfiguration(final LineRepository lineRepository,
            final StationRepository stationRepository) {
            this.lineRepository = lineRepository;
            this.stationRepository = stationRepository;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            configureLines();
            configureStations();
        }

        /*
         *
         */
        private void configureLines() {
            final Line line1 = new Line("1호선", "bg-blue-700", LocalTime.of(5, 10), LocalTime.of(23, 0), 5);
            line1.addLineStation(new LineStation(null, 1L, 2, 1));
            line1.addLineStation(new LineStation(1L, 2L, 2, 1));
            line1.addLineStation(new LineStation(2L, 3L, 2, 1));
            line1.addLineStation(new LineStation(3L, 4L, 2, 1));
            line1.addLineStation(new LineStation(4L, 5L, 2, 1));
            line1.addLineStation(new LineStation(5L, 20L, 2, 1));
            line1.addLineStation(new LineStation(20L, 21L, 2, 1));
            line1.addLineStation(new LineStation(21L, 22L, 2, 1));
            line1.addLineStation(new LineStation(22L, 23L, 2, 1));
            lineRepository.save(line1);

            final Line line2 = new Line("2호선", "bg-green-500", LocalTime.of(6, 0), LocalTime.of(23, 30), 8);
            line2.addLineStation(new LineStation(null, 23L, 1, 1));
            line2.addLineStation(new LineStation(23L, 25L, 1, 1));
            line2.addLineStation(new LineStation(25L, 24L, 1, 1));
            line2.addLineStation(new LineStation(24L, 6L, 1, 1));
            line2.addLineStation(new LineStation(6L, 7L, 1, 1));
            line2.addLineStation(new LineStation(7L, 8L, 1, 1));
            line2.addLineStation(new LineStation(8L, 9L, 1, 1));
            line2.addLineStation(new LineStation(9L, 10L, 1, 1));
            line2.addLineStation(new LineStation(10L, 11L, 1, 1));
            line2.addLineStation(new LineStation(11L, 12L, 1, 1));
            line2.addLineStation(new LineStation(12L, 1L, 1, 1));
            lineRepository.save(line2);

            final Line line4 = new Line("4호선", "bg-blue-500", LocalTime.of(5, 50), LocalTime.of(22, 50), 6);
            line4.addLineStation(new LineStation(null, 2L, 1, 1));
            line4.addLineStation(new LineStation(2L, 13L, 1, 1));
            line4.addLineStation(new LineStation(13L, 14L, 1, 1));
            line4.addLineStation(new LineStation(14L, 15L, 1, 1));
            line4.addLineStation(new LineStation(15L, 16L, 1, 1));
            line4.addLineStation(new LineStation(16L, 17L, 1, 1));
            line4.addLineStation(new LineStation(17L, 18L, 1, 1));
            lineRepository.save(line4);

            final Line line8 = new Line("8호선", "bg-pink-500", LocalTime.of(6, 20), LocalTime.of(23, 40), 10);
            line8.addLineStation(new LineStation(null, 19L, 1, 1));
            lineRepository.save(line8);
        }

        private void configureStations() {
            stationRepository.save(new Station("시청"));
            stationRepository.save(new Station("서울역"));
            stationRepository.save(new Station("남영"));
            stationRepository.save(new Station("용산"));
            stationRepository.save(new Station("노량진"));
            stationRepository.save(new Station("당산"));
            stationRepository.save(new Station("합정"));
            stationRepository.save(new Station("홍대입구"));
            stationRepository.save(new Station("신촌"));
            stationRepository.save(new Station("이대"));
            stationRepository.save(new Station("아현"));
            stationRepository.save(new Station("충정로"));
            stationRepository.save(new Station("숙대입구"));
            stationRepository.save(new Station("삼각지"));
            stationRepository.save(new Station("신용산"));
            stationRepository.save(new Station("이촌"));
            stationRepository.save(new Station("동작"));
            stationRepository.save(new Station("이수"));
            stationRepository.save(new Station("잠실"));
            stationRepository.save(new Station("대방"));
            stationRepository.save(new Station("신길"));
            stationRepository.save(new Station("영등포"));
            stationRepository.save(new Station("신도림"));
            stationRepository.save(new Station("영등포구청"));
            stationRepository.save(new Station("문래"));
        }
    }

}
