package wooteco.subway.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.exception.InvalidSectionInsertException;
import wooteco.subway.exception.NotFoundSectionException;

public class LineSections {

    public static final int FIRST_SECTION_INDEX = 0;

    private final List<Section> sections;

    public LineSections(List<Section> sections) {
        this.sections = sections;
    }

    public void validateSection(long upStationId, long downStationId, int distance) {
        validateBothStationExist(upStationId, downStationId);
        validateNoneStationExist(upStationId, downStationId);
        validateDistance(upStationId, downStationId, distance);
    }

    private void validateBothStationExist(long upStationId, long downStationId) {
        if (existByStationId(upStationId)
                && existByStationId(downStationId)) {
            throw new InvalidSectionInsertException("상행, 하행이 대상 노선에 둘 다 존재합니다.");
        }
    }

    private boolean existByStationId(long stationId) {
        return existByUpStationId(stationId) || existByDownStationId(stationId);
    }

    private boolean existByUpStationId(long stationId) {
        return sections.stream()
                .map(Section::getUpStationId)
                .anyMatch(id -> id == stationId);
    }

    private boolean existByDownStationId(long stationId) {
        return sections.stream()
                .map(Section::getDownStationId)
                .anyMatch(id -> id == stationId);
    }

    private void validateNoneStationExist(long upStationId, long downStationId) {
        if (!existByStationId(upStationId)
                && !existByStationId(downStationId)) {
            throw new InvalidSectionInsertException("상행, 하행이 대상 노선에 둘 다 존재하지 않습니다.");
        }
    }

    private void validateDistance(long upStationId, long downStationId, int distance) {
        if (isInvalidDistanceWithDownStationOverlap(downStationId, distance)
                || isInvalidDistanceWithUpStationOverlap(upStationId, distance)) {
            throw new InvalidSectionInsertException(
                    "역 사이에 새로운 역을 등록할 경우, 기존 역 사이 길이보다 크거나 같으면 등록할 수 없습니다.");
        }
    }

    private boolean isInvalidDistanceWithDownStationOverlap(long downStationId, int distance) {
        return existByDownStationId(downStationId)
                && findDistanceById(findIdByDownStationId(downStationId)) <= distance;
    }

    private boolean isInvalidDistanceWithUpStationOverlap(long upStationId, int distance) {
        return existByUpStationId(upStationId)
                && findDistanceById(findIdByUpStationId(upStationId)) <= distance;
    }

    private long findIdByUpStationId(long stationId) {
        return sections.stream()
                .filter(section -> section.isSameUpStationId(stationId))
                .map(Section::getId)
                .findAny()
                .orElseThrow(() -> new NotFoundSectionException("일치하는 Section이 존재하지 않습니다."));
    }

    private long findIdByDownStationId(long stationId) {
        return sections.stream()
                .filter(section -> section.isSameDownStationId(stationId))
                .map(Section::getId)
                .findAny()
                .orElseThrow(() -> new NotFoundSectionException("일치하는 Section이 존재하지 않습니다."));
    }

    private int findDistanceById(Long id) {
        return sections.stream()
                .filter(section -> section.isSameId(id))
                .findAny()
                .map(Section::getDistance)
                .orElseThrow(() -> new NotFoundSectionException("일치하는 Section이 존재하지 않습니다."));
    }

    public SectionUpdateResult findOverlapSection(long upStationId, long downStationId, int distance) {
        if (existByUpStationId(upStationId)) {
            Section oldSection = findById(findIdByUpStationId(upStationId));
            return separateSectionInExistUpMatchCase(upStationId, downStationId, distance, oldSection);
        }
        if (existByDownStationId(downStationId)) {
            Section oldSection = findById(findIdByDownStationId(downStationId));
            return separateSectionInExistDownMatchCase(upStationId, downStationId, distance, oldSection);
        }
        if (existByUpStationId(downStationId)) {
            Section oldSection = findById(findIdByUpStationId(downStationId));
            return addSectionFront(upStationId, downStationId, distance, oldSection);
        }
        Section oldSection = findById(findIdByDownStationId(upStationId));
        return addSectionBack(upStationId, downStationId, distance, oldSection);
    }

    private SectionUpdateResult separateSectionInExistUpMatchCase(
            long upStationId, long downStationId, int distance, Section oldSection) {
        return new SectionUpdateResult(
                new Section(
                        oldSection.getId(),
                        oldSection.getLineId(),
                        upStationId,
                        downStationId,
                        distance,
                        oldSection.getLineOrder()
                ),
                Section.createOf(
                        oldSection.getLineId(),
                        downStationId,
                        oldSection.getDownStationId(),
                        oldSection.getDistance() - distance,
                        oldSection.getLineOrder() + 1
                )
        );
    }

    private SectionUpdateResult separateSectionInExistDownMatchCase(
            long upStationId, long downStationId, int distance, Section oldSection) {
        return new SectionUpdateResult(
                new Section(oldSection.getId(),
                        oldSection.getLineId(),
                        oldSection.getUpStationId(),
                        upStationId,
                        oldSection.getDistance() - distance,
                        oldSection.getLineOrder()
                ),
                Section.createOf(
                        oldSection.getLineId(),
                        upStationId,
                        downStationId,
                        distance,
                        oldSection.getLineOrder() + 1
                )
        );
    }

    private SectionUpdateResult addSectionBack(long upStationId, long downStationId, int distance, Section oldSection) {
        return new SectionUpdateResult(
                oldSection,
                Section.createOf(
                        oldSection.getLineId(),
                        upStationId,
                        downStationId,
                        distance,
                        oldSection.getLineOrder() + 1
                )
        );
    }

    private SectionUpdateResult addSectionFront(long upStationId, long downStationId, int distance, Section oldSection) {
        return new SectionUpdateResult(
                oldSection,
                Section.createOf(
                        oldSection.getLineId(),
                        upStationId,
                        downStationId,
                        distance,
                        oldSection.getLineOrder()
                )
        );
    }

    private Section findById(long sectionId) {
        return sections.stream()
                .filter(section -> section.isSameId(sectionId))
                .findAny()
                .orElseThrow(() -> new NotFoundSectionException("일치하는 Section이 존재하지 않습니다."));
    }

    public List<Long> getStationIds() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Section> sortedSections = createSortedSections();
        List<Long> stationIds = upStationIdsOf(sortedSections);
        stationIds.add(sortedSections.get(sortedSections.size() - 1).getDownStationId());

        return stationIds;
    }

    private List<Long> upStationIdsOf(List<Section> sections) {
        return sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toList());
    }

    private List<Section> createSortedSections() {
        return sections.stream()
                .sorted(Comparator.comparingLong(Section::getLineOrder))
                .collect(Collectors.toList());
    }

    public boolean hasTwoSection() {
        return sections.size() == 2;
    }

    public Section getSingleDeleteSection() {
        return sections.get(FIRST_SECTION_INDEX);
    }

    public Section getUpsideSection() {
        return createSortedSections().get(FIRST_SECTION_INDEX);
    }

    public Section getDownsideSection() {
        List<Section> sortedSections = createSortedSections();
        return sortedSections.get(sortedSections.size() - 1);
    }
}
