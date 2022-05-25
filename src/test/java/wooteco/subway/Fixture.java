package wooteco.subway;

import java.util.List;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class Fixture {

    // 2호선
    public static final Station 교대역 = new Station("교대역");
    public static final Station 강남역 = new Station("강남역");
    public static final Station 역삼역 = new Station("역삼역");
    public static final Station 선릉역 = new Station("선릉역");
    public static final Station 삼성역 = new Station("삼성역");
    public static final Station 종합운동장역 = new Station("중합운동장역");

    // 수인선
    public static final Station 한티역 = new Station("한티역");
    public static final Station 도곡역 = new Station("도곡역");
    public static final Station 선정릉역 = new Station("선정릉역");
    public static final Station 삼성중앙역 = new Station("삼성중앙역");


    // 9호선
    public static final Station 봉은사역 = new Station("봉은사역");
    public static final Station 삼전역 = new Station("삼전역");

    // 신분당선
    public static final Station 양재역 = new Station("양재역");
    public static final Station 양재시민의숲역 = new Station("양재시민의숲역");


    // 2호선 구간 -> 2
    public static final Section 교대_강남 = new Section(2L, 교대역, 강남역, 5);
    public static final Section 강남_역삼 = new Section(2L, 강남역, 역삼역, 5);
    public static final Section 역삼_선릉 = new Section(2L, 역삼역, 선릉역, 5);
    public static final Section 선릉_삼성 = new Section(2L, 선릉역, 삼성역, 5);
    public static final Section 삼성_종합운동장 = new Section(2L, 삼성역, 종합운동장역, 5);

    // 수인선 구간 -> 1
    public static final Section 선릉_선정릉 = new Section(1L, 선릉역, 선정릉역, 15);


    // 9호선 구간 -> 9
    public static final Section 선정릉_삼성중앙 = new Section(9L, 선정릉역, 삼성중앙역, 10);
    public static final Section 삼성중앙_봉은사 = new Section(9L, 삼성중앙역, 봉은사역, 15);
    public static final Section 봉은사_종합운동장 = new Section(9L, 봉은사역, 종합운동장역, 5);
    public static final Section 종합운동장_삼전 = new Section(9L, 종합운동장역, 삼전역, 5);


    // 신분당선 구간 -> 11
    public static final Section 강남_양재 = new Section(11L, 강남역, 양재역, 10);
    public static final Section 양재_양재시민의숲 = new Section(11L, 양재역, 양재시민의숲역, 10);


    // 전체 구간
    public static final List<Section> 이호선_수인선_9호선_구간 = List.of(
            강남_역삼, 역삼_선릉, 선릉_삼성, 삼성_종합운동장, 선릉_선정릉, 선정릉_삼성중앙, 삼성중앙_봉은사, 봉은사_종합운동장, 종합운동장_삼전);

    public static final List<Section> 이호선_신분당선_구간 = List.of(
            교대_강남, 강남_양재, 양재_양재시민의숲);

    public static final List<Section> 이호선_신분당선_구간_중_신분당선이_하나만_포함된_구간 = List.of(
            교대_강남, 강남_양재, 강남_역삼, 역삼_선릉);
}
