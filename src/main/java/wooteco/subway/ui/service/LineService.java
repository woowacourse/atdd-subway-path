package wooteco.subway.ui.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.response.LineCreateResponse;

@Service
public class LineService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public LineCreateResponse create(LineCreateRequest lineCreateRequest) {
        String name = lineCreateRequest.getName();
        String color = lineCreateRequest.getColor();
        Section section = getSection(lineCreateRequest);
        int extraFare = lineCreateRequest.getExtraFare();
        Line line = new Line(name, color, extraFare, section);
        return save(section, line);
    }

    private Section getSection(LineCreateRequest lineCreateRequest) {
        Station upStation = stationDao.findById(lineCreateRequest.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 상행역이 존재하지 않습니다."));
        Station downStation = stationDao.findById(lineCreateRequest.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 하행역이 존재하지 않습니다."));
        return new Section(upStation, downStation, lineCreateRequest.getDistance());
    }

    private LineCreateResponse save(Section section, Line line) {
        try {
            Line createdLine = lineDao.save(line);
            sectionDao.save(section, createdLine.getId());
            return new LineCreateResponse(createdLine);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
        }
    }

    public List<LineCreateResponse> findAll() {
        List<Line> lines = lineDao.findAll();
        return lines.stream()
                .map(LineCreateResponse::new)
                .collect(Collectors.toList());
    }

    public LineCreateResponse findById(Long id) {
        Line line = lineDao.findById(id).orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 노선이 존재하지 않습니다."));
        return new LineCreateResponse(line);
    }

    public void modify(Long id, LineCreateRequest lineCreateRequest) {
        Line line = new Line(id, lineCreateRequest.getName(), lineCreateRequest.getColor(), 900);
        lineDao.update(line);
    }

    public void delete(Long id) {
        lineDao.delete(id);
    }
}
