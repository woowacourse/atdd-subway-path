package wooteco.subway.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.exception.DuplicateLineException;
import wooteco.subway.exception.NotFoundLineException;
import wooteco.subway.exception.NotFoundStationException;

@Transactional
@Service
public class LineService {

    private static final String NOT_FOUND_STATION_ERROR_MESSAGE = "해당하는 역이 존재하지 않습니다.";
    private static final String NOT_FOUND_LINE_ERROR_MESSAGE = "해당하는 노선이 존재하지 않습니다.";
    private static final String DUPLICATE_LINE_ERROR_MESSAGE = "같은 이름의 노선이 존재합니다.";
    private static final String NOT_FOUND_STATION_DELETE_ERROR_MESSAGE = "역이 노선에 등록되어 있지 않다면 삭제할 수 없습니다.";
    private static final String SECTION_HAS_NOT_ANY_STATION_ERROR_MESSAGE = "등록하려는 구간 중 하나 이상의 역은 무조건 노선에 등록되어 있어야 합니다.";
    private static final String SECTION_HAS_ALL_STATION_ERROR_MESSAGE = "상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없습니다.";
    private static final String LAST_SECTION_DELETE_ERROR_MESSAGE = "구간이 하나인 노선에서 마지막 구간을 제거할 수 없습니다.";

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        checkDuplicateLine(lineRequest);
        final Line line = new Line(lineRequest.getName(), lineRequest.getColor());
        final Station upStation = stationDao.findById(lineRequest.getUpStationId()).
                orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));
        final Station downStation = stationDao.findById(lineRequest.getDownStationId()).
                orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));

        final Line savedLine = lineDao.save(line);
        final Section section = new Section(savedLine, upStation, downStation,
                lineRequest.getDistance());
        sectionDao.save(section);

        return LineResponse.of(savedLine, Arrays.asList(upStation, downStation));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineDao.findAll();

        List<LineResponse> responses = new ArrayList<>();
        for (Line line : lines) {
            Sections sections = getSections(line);
            responses.add(LineResponse.of(line, sections.getStations()));
        }

        return responses;
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        final Line line = lineDao.findById(id).
                orElseThrow(() -> new NotFoundLineException(NOT_FOUND_LINE_ERROR_MESSAGE));
        final Sections sections = getSections(line);

        return LineResponse.of(line, sections.getStations());
    }

    public void updateLine(Long id, String name, String color) {
        checkNotFoundLine(id);
        lineDao.updateById(id, name, color);
    }

    public void deleteLine(Long id) {
        checkNotFoundLine(id);
        lineDao.deleteById(id);
    }

    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        final Line line = lineDao.findById(lineId).
                orElseThrow(() -> new NotFoundLineException(NOT_FOUND_LINE_ERROR_MESSAGE));
        final Sections sections = getSections(line);

        final Station upStation = stationDao.findById(sectionRequest.getUpStationId()).
                orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));
        final Station downStation = stationDao.findById(sectionRequest.getDownStationId()).
                orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));
        final int distance = sectionRequest.getDistance();
        Section newSection = new Section(line, upStation, downStation, distance);

        checkSectionHasNotAnyStation(sections, upStation, downStation);
        checkSectionHasAllStation(sections, upStation, downStation);

        if (sections.isSplit(upStation, downStation)) {
            Section updatedSection = sections.splitSection(newSection);
            sectionDao.update(updatedSection);
        }
        sectionDao.save(new Section(line, upStation, downStation, distance));
    }

    public void deleteSection(Long lineId, Long stationId) {
        final Line line = lineDao.findById(lineId).
                orElseThrow(() -> new NotFoundLineException(NOT_FOUND_LINE_ERROR_MESSAGE));
        final Station station = stationDao.findById(stationId).
                orElseThrow(() -> new NotFoundStationException(NOT_FOUND_STATION_ERROR_MESSAGE));
        final Sections sections = getSections(line);
        checkOnlyOneSection(sections);
        checkNotContainStation(sections, station);

        Station lastUpStation = sections.getLastUpStation(sections.getSections());
        if (lastUpStation.isSameStation(station)) {
            Section upSection = sectionDao.findByLineIdAndUpStationId(lineId, stationId);
            sectionDao.deleteById(upSection.getId());
            return;
        }

        Station lastDownStation = sections.getLastDownStation(sections.getSections());
        if (lastDownStation.isSameStation(station)) {
            Section downSection = sectionDao.findByLineIdAndDownStationId(lineId, stationId);
            sectionDao.deleteById(downSection.getId());
            return;
        }

        if (sections.hasSameUpStation(station) && sections.hasSameDownStation(station)) {
            Section upSection = sectionDao.findByLineIdAndDownStationId(lineId, stationId);
            Section downSection = sectionDao.findByLineIdAndUpStationId(lineId, stationId);
            int upSectionDistance = upSection.getDistance();
            int downSectionDistance = downSection.getDistance();
            sectionDao.deleteById(downSection.getId());
            sectionDao.update(new Section(upSection.getId(), line, upSection.getUpStation(),
                    downSection.getDownStation(), upSectionDistance + downSectionDistance));
        }
    }

    private void checkNotFoundLine(Long id) {
        lineDao.findById(id).
                orElseThrow(() -> new NotFoundLineException(NOT_FOUND_LINE_ERROR_MESSAGE));
    }

    private void checkDuplicateLine(LineRequest lineRequest) {
        if (lineDao.hasLine(lineRequest.getName())) {
            throw new DuplicateLineException(DUPLICATE_LINE_ERROR_MESSAGE);
        }
    }

    private Sections getSections(Line line) {
        final List<Section> sections = sectionDao.findByLineId(line.getId());
        return new Sections(sections);
    }

    private void checkNotContainStation(Sections sections, Station station) {
        if (!sections.hasStation(station)) {
            throw new NotFoundStationException(NOT_FOUND_STATION_DELETE_ERROR_MESSAGE);
        }
    }

    private void checkSectionHasNotAnyStation(Sections sections, Station upStation,
            Station downStation) {
        if (!sections.hasStation(upStation) && !sections.hasStation(downStation)) {
            throw new NotFoundStationException(SECTION_HAS_NOT_ANY_STATION_ERROR_MESSAGE);
        }
    }

    private void checkSectionHasAllStation(Sections sections, Station upStation,
            Station downStation) {
        if (sections.hasSameUpStation(upStation) && sections.hasSameDownStation(downStation)) {
            throw new IllegalArgumentException(SECTION_HAS_ALL_STATION_ERROR_MESSAGE);
        }
    }

    private void checkOnlyOneSection(Sections sections) {
        if (sections.hasOnlyOneSection()) {
            throw new IllegalArgumentException(LAST_SECTION_DELETE_ERROR_MESSAGE);
        }
    }
}
