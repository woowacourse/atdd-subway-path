package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.controller.dto.LineRequest;
import wooteco.subway.controller.dto.SectionRequest;
import wooteco.subway.dao.repository.LineRepository;
import wooteco.subway.dao.repository.SectionRepository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.SectionsDirtyChecker;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.LineDto;
import wooteco.subway.service.dto.StationDto;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository,
        StationService stationService) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineDto create(LineRequest lineRequest) {
        validateNameNotDuplicated(lineRequest.getName());
        Long lineId = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(),
            lineRequest.getExtraFare(), List.of(getSection(lineRequest.toSectionRequest()))));
        return LineDto.from(lineRepository.findById(lineId));
    }

    private void validateNameNotDuplicated(String name) {
        if (lineRepository.existsByName(name)) {
            throw new IllegalArgumentException("해당 이름의 지하철 노선이 이미 존재합니다");
        }
    }

    public List<LineDto> listLines() {
        return lineRepository.findAll().stream()
            .map(LineDto::from)
            .collect(Collectors.toList());
    }

    public LineDto findOne(Long id) {
        return LineDto.from(lineRepository.findById(id));
    }

    @Transactional
    public LineDto update(Line line) {
        lineRepository.update(line);
        return findOne(line.getId());
    }

    @Transactional
    public void remove(Long id) {
        lineRepository.remove(id);
    }

    @Transactional
    public void addSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id);
        SectionsDirtyChecker checker = SectionsDirtyChecker.from(line.getSections());

        line.addSection(getSection(sectionRequest));

        executeDirtyChecking(id, line, checker);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId);
        SectionsDirtyChecker checker = SectionsDirtyChecker.from(line.getSections());

        line.deleteSectionByStation(stationId);

        executeDirtyChecking(lineId, line, checker);
    }

    private void executeDirtyChecking(Long lineId, Line line, SectionsDirtyChecker checker) {
        checker.findUpdated(line.getSections())
            .executeEach(sectionRepository::update);
        checker.findDeleted(line.getSections())
            .executeEach(section -> sectionRepository.remove(section.getId()));
        checker.findSaved(line.getSections())
            .executeEach(section -> sectionRepository.save(lineId, section));
    }

    private Section getSection(SectionRequest sectionRequest) {
        Station upStation = getStation(stationService.findOne(sectionRequest.getUpStationId()));
        Station downStation = getStation(stationService.findOne(sectionRequest.getDownStationId()));
        return new Section(upStation, downStation, sectionRequest.getDistance());
    }

    private Station getStation(StationDto stationDto) {
        return new Station(stationDto.getId(), stationDto.getName());
    }
}
