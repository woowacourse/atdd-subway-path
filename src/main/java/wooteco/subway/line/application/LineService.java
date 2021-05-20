package wooteco.subway.line.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.application.dto.LineRequestDto;
import wooteco.subway.line.application.dto.LineResponseDto;
import wooteco.subway.line.application.dto.SectionRequestDto;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.section.Section;
import wooteco.subway.line.infrastructure.dao.LineDao;
import wooteco.subway.line.infrastructure.dao.SectionDao;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public LineResponseDto saveLine(LineRequestDto lineRequestDto) {
        Line persistLine = lineDao.insert(new Line(lineRequestDto.getName(), lineRequestDto.getColor()));
        persistLine.addSection(addInitSection(persistLine, lineRequestDto));

        return LineResponseDto.of(persistLine);
    }

    private Section addInitSection(Line line, LineRequestDto lineRequestDto) {
        if (lineRequestDto.getUpStationId() != null && lineRequestDto.getDownStationId() != null) {
            Station upStation = stationService.findStationById(lineRequestDto.getUpStationId());
            Station downStation = stationService.findStationById(lineRequestDto.getDownStationId());
            Section section = new Section(upStation, downStation, lineRequestDto.getDistance());
            return sectionDao.insert(line, section);
        }
        return null;
    }

    public List<LineResponseDto> findLineResponses() {
        List<Line> persistLines = lineDao.findAll();

        return persistLines.stream()
                .map(LineResponseDto::of)
                .collect(toList());
    }

    public LineResponseDto findLineResponseById(Long id) {
        Line persistLine = findLineById(id);

        return LineResponseDto.of(persistLine);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequestDto lineRequestDto) {
        lineDao.update(new Line(id, lineRequestDto.getName(), lineRequestDto.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    public void addLineStation(Long lineId, SectionRequestDto sectionRequestDto) {
        Line line = findLineById(lineId);

        Station upStation = stationService.findStationById(sectionRequestDto.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequestDto.getDownStationId());

        line.addSection(upStation, downStation, sectionRequestDto.getDistance());

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = stationService.findStationById(stationId);
        line.removeSection(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertSections(line);
    }

}
