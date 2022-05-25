package wooteco.subway.domain;

public class SectionUpdateResult {

    private final Section updatedSection;
    private final Section addedSection;

    public SectionUpdateResult(final Section updatedSection, final Section addedSection) {
        this.updatedSection = updatedSection;
        this.addedSection = addedSection;
    }

    public Section getUpdatedSection() {
        return updatedSection;
    }

    public Section getAddedSection() {
        return addedSection;
    }
}
