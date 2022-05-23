package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

public class ServiceTestFixture {

    public static void deleteAllStation(StationRepository stationRepository) {
        List<Station> stations = stationRepository.findAll();

        List<Long> stationIds = stations.stream()
            .map(Station::getId)
            .collect(Collectors.toList());

        for (Long stationId : stationIds) {
            stationRepository.deleteById(stationId);
        }
    }

    public static void deleteAllLine(LineRepository lineRepository) {
        List<Line> lines = lineRepository.findAll();

        List<Long> lineIds = lines.stream()
            .map(Line::getId)
            .collect(Collectors.toList());

        for (Long lineId : lineIds) {
            lineRepository.deleteById(lineId);
        }
    }

    public static void deleteAllSection(SectionRepository sectionRepository) {
        List<Section> sections = sectionRepository.findAll();

        List<Long> sectionIds = sections.stream()
            .map(Section::getId)
            .collect(Collectors.toList());

        for (Long sectionId : sectionIds) {
            sectionRepository.deleteById(sectionId);
        }
    }
}
