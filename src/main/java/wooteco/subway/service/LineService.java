package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.LineUpdateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.exception.duplicate.DuplicateLineException;
import wooteco.subway.exception.notfound.LineNotFoundException;

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

    @Transactional
    public LineResponse save(LineCreateRequest lineCreateRequest) {
        Line persistLine = saveLine(lineCreateRequest);

        Section section = saveSectionByLineRequest(lineCreateRequest, persistLine);
        List<Station> stations = List.of(section.getUpStation(), section.getDownStation());

        return new LineResponse(persistLine, stations);
    }

    private Line saveLine(LineCreateRequest lineCreateRequest) {
        Line line = new Line(lineCreateRequest.getName(), lineCreateRequest.getColor(),
                lineCreateRequest.getExtraFare());
        validateUnique(line);
        return lineDao.save(line);
    }

    private void validateUnique(Line line) {
        if (lineDao.existsName(line)) {
            throw new DuplicateLineException("이미 존재하는 노선 이름입니다.");
        }
        if (lineDao.existsColor(line)) {
            throw new DuplicateLineException("이미 존재하는 노선 색상입니다.");
        }
    }

    private Section saveSectionByLineRequest(LineCreateRequest lineCreateRequest, Line line) {
        Section section = new Section(
                line,
                stationService.findById(lineCreateRequest.getUpStationId()),
                stationService.findById(lineCreateRequest.getDownStationId()),
                lineCreateRequest.getDistance()
        );
        return sectionDao.save(section);
    }

    @Transactional
    public void saveSectionBySectionRequest(Long lineId, SectionRequest sectionRequest) {
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException("존재하지 않는 노선입니다."));
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        int distance = sectionRequest.getDistance();
        Section newSection = new Section(line, upStation, downStation, distance);

        addNewSection(line, newSection);
    }

    private void addNewSection(Line line, Section newSection) {
        Sections originSections = new Sections(sectionDao.findAllByLine(line));
        Sections newSections = new Sections(originSections.getValues());

        newSections.addSection(newSection);
        saveModifications(line, originSections, newSections);
    }

    private void saveModifications(Line line, Sections originSections, Sections newSections) {
        List<Section> removedSections = originSections.getNotContainSections(newSections);
        deleteRemovedSections(line, removedSections);
        List<Section> addedSections = newSections.getNotContainSections(originSections);
        saveAddedLine(line, addedSections);
    }

    private void deleteRemovedSections(Line line, List<Section> deleteSections) {
        for (Section section : deleteSections) {
            sectionDao.deleteByLineAndSection(line, section);
        }
    }

    private void saveAddedLine(Line line, List<Section> insertSections) {
        for (Section section : insertSections) {
            sectionDao.save(section);
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        Sections sections = new Sections(sectionDao.findAllByLine(line));
        List<Station> sortedStations = sections.getSortedStations();
        return new LineResponse(line, sortedStations);
    }

    @Transactional(readOnly = true)
    public LineResponse find(Long id) {
        Line line = lineDao.findById(id)
                .orElseThrow(() -> new LineNotFoundException("존재하지 않는 노선입니다."));
        Sections sections = new Sections(sectionDao.findAllByLine(line));
        return new LineResponse(line, sections.getSortedStations());
    }

    @Transactional
    public void update(Long id, LineUpdateRequest lineUpdateRequest) {
        validateExist(id);
        Line line = new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(),
                lineUpdateRequest.getExtraFare());
        validateUnique(line);
        lineDao.updateById(id, line);
    }

    private void validateExist(Long id) {
        if (!lineDao.existsId(id)) {
            throw new LineNotFoundException("존재하지 않는 노선입니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        validateExist(id);
        lineDao.deleteById(id);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Station station = stationService.findById(stationId);
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException("존재하지 않는 노선입니다."));
        Sections originSections = new Sections(sectionDao.findAllByLine(line));
        Sections newSections = new Sections(originSections.getValues());

        newSections.removeSectionByStation(station);
        saveModifications(line, originSections, newSections);
    }

}
