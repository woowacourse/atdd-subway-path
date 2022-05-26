package wooteco.subway.service;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineEditRequest;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.exception.line.DuplicateLineNameException;
import wooteco.subway.exception.line.NoSuchLineException;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository,
                       StationRepository stationRepository,
                       SectionRepository sectionRepository
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse save(LineRequest lineRequest) {
        Section section = new Section(stationRepository.findById(lineRequest.getUpStationId()),
                stationRepository.findById(lineRequest.getDownStationId()), lineRequest.getDistance());
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare(), new Sections(section));
        try {
            Line savedLine = lineRepository.save(line);
            return LineResponse.from(savedLine);
        } catch (DuplicateKeyException e) {
            throw new DuplicateLineNameException();
        }
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long lineId) {
        try {
            return LineResponse.from(lineRepository.findById(lineId));
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchLineException();
        }
    }

    @Transactional
    public void update(Long lineId, LineEditRequest lineEditRequest) {
        Line line = lineRepository.findById(lineId);
        line.updateNameAndColor(lineEditRequest.getName(), lineEditRequest.getColor());
        try {
            lineRepository.update(line);
        } catch (DuplicateKeyException e) {
            throw new DuplicateLineNameException();
        }
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findById(lineId);
        Station up = stationRepository.findById(request.getUpStationId());
        Station down = stationRepository.findById(request.getDownStationId());
        Section section = new Section(up, down, request.getDistance());

        Sections before = new Sections(line.getSections());
        line.add(section);
        deleteOldSectionsAndInsertNew(line, before);
    }

    private void deleteOldSectionsAndInsertNew(Line line, Sections before) {
        Sections after = new Sections(line.getSections());
        List<Section> deleteTargets = before.findDifferentSections(after);
        List<Section> insertTargets = after.findDifferentSections(before);

        List<Long> deleteSectionIds = deleteTargets.stream()
                .map(Section::getId)
                .collect(Collectors.toList());

        sectionRepository.deleteByIdIn(deleteSectionIds);

        for (Section updateTarget : insertTargets) {
            sectionRepository.save(line.getId(),
                    new SectionRequest(updateTarget.getUp().getId(), updateTarget.getDown().getId(),
                            updateTarget.getDistance()));
        }
    }

    @Transactional
    public void deleteById(Long lineId) {
        lineRepository.delete(lineId);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId);
        Station station = stationRepository.findById(stationId);

        Sections before = new Sections(line.getSections());
        line.delete(station);
        deleteOldSectionsAndInsertNew(line, before);
    }
}
