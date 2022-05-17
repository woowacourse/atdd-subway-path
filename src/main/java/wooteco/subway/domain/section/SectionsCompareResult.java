package wooteco.subway.domain.section;

import java.util.List;
import java.util.Objects;

public class SectionsCompareResult {

    private final List<Section> newSections;
    private final List<Section> oldSections;

    public SectionsCompareResult(List<Section> newSections,
                                 List<Section> oldSections) {
        this.newSections = newSections;
        this.oldSections = oldSections;
    }

    public List<Section> getNewSections() {
        return newSections;
    }

    public List<Section> getOldSections() {
        return oldSections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionsCompareResult that = (SectionsCompareResult) o;
        return Objects.equals(newSections, that.newSections)
                && Objects.equals(oldSections, that.oldSections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newSections, oldSections);
    }

    @Override
    public String toString() {
        return "SectionsCompareResult{" +
                "newSections=" + newSections +
                ", oldSections=" + oldSections +
                '}';
    }
}
