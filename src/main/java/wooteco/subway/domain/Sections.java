package wooteco.subway.domain;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Sections {

    private static final int SIZE_ONE = 1;
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean hasOneStation(Long stationId) {
        long count = sections.stream()
                .filter(it -> it.isSameUpStation(stationId) || it.isSameDownStation(stationId))
                .count();
        return count == SIZE_ONE;
    }

    public Long getSectionId(Long stationId) {
        return sections.stream()
                .filter(it -> it.isSameUpStation(stationId) || it.isSameDownStation(stationId))
                .map(Section::getId)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제하려는 구간이 존재하지 않습니다."));
    }

    public Section getUpStationSection(Long stationId) {
        return sections.stream()
                .filter(it -> it.isSameDownStation(stationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제할 대상의 상행종점을 찾을 수 없습니다."));
    }

    public Section getDownStationSection(Long stationId) {
        return sections.stream()
                .filter(it -> it.isSameUpStation(stationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 삭제할 대상의 하행종점을 찾을 수 없습니다."));
    }

    public void validateAddNewSection(Section newSection) {
        validateSameStation(newSection.getUpStationId(), newSection.getDownStationId());
        validateDistance(newSection.getDistance());
        validateExistSection(newSection);
    }

    private void validateSameStation(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new IllegalArgumentException("[ERROR] 동일한 지하철역을 입력할 수 없습니다.");
        }
    }

    private boolean existSection(Long stationId) {
        return sections.stream()
                .anyMatch(it -> it.isSameUpStation(stationId) || it.isSameDownStation(stationId));
    }

    private void validateExistSection(Section section) {
        if (sections.isEmpty()) {
            return;
        }
        boolean hasSection1 = existSection(section.getUpStationId());
        boolean hasSection2 = existSection(section.getDownStationId());

        if (hasSection1 && hasSection2) {
            throw new IllegalArgumentException("[ERROR] 이미 등록되어 있어 추가할 수 없습니다.");
        }
        if (!hasSection1 && !hasSection2) {
            throw new IllegalArgumentException("[ERROR] 지하철역이 존재하지 않습니다.");
        }
    }

    private void validateDistance(int distance) {
        if (distance < SIZE_ONE) {
            throw new IllegalArgumentException("[ERROR] 거리는 양수입니다.");
        }
    }

    public boolean isUpStationId(Long upStationId) {
        return sections.stream().anyMatch(it -> it.isSameUpStation(upStationId));
    }

    public boolean isDownStationId(Long downStationId) {
        return sections.stream().anyMatch(it -> it.isSameDownStation(downStationId));
    }

    public Section getSectionByUpStationId(Long upStationId) {
        return sections.stream()
                .filter(it -> it.isSameUpStation(upStationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 대상이 존재하지 않습니다."));
    }

    public Section getSectionByDownStationId(Long downStationId) {
        return sections.stream()
                .filter(it -> it.isSameDownStation(downStationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 대상이 존재하지 않습니다."));
    }

    public void validateSize() {
        if (sections.size() == SIZE_ONE) {
            throw new IllegalArgumentException("[ERROR] 더 이상 삭제할 수 없습니다.");
        }
    }

    public List<Section> getSections() {
        return sections;
    }

    private List<Long> getUpStationIds() {
        return sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());
    }

    private List<Long> getDownStationIds() {
        return sections.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toList());
    }

    public Long findFinalUpStationId() {
        return getUpStationIds().stream()
                .filter(it -> !getDownStationIds().contains(it))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 상행역 종점 조회 오류가 발생했습니다."));
    }

    public boolean hasUpStationId(Long upStationId) {
        return getUpStationIds().contains(upStationId);
    }
}
