package wooteco.subway.member.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import wooteco.subway.exception.notfound.MemberNotFoundException;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberDaoTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";
    private static final int AGE = 12;

    private final MemberDao memberDao;

    public MemberDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.memberDao = new MemberDao(jdbcTemplate, dataSource);
    }

    @DisplayName("회원을 등록한다.")
    @Test
    void insert() {
        //given
        Member member = new Member(EMAIL, PASSWORD, AGE);

        //when
        Member savedMember = memberDao.insert(member);

        //then
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("이미 존재하는 회원을 등록한다.")
    @Test
    void duplicateInsert() {
        //given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        memberDao.insert(member);

        //then
        assertThatThrownBy(() -> memberDao.insert(member))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("회원 정보를 수정한다.")
    @Test
    void update() {
        //given
        Member 등록된_회원 = 회원_등록();
        Member 수정된_회원 = new Member(등록된_회원.getId(), "update@email.com", "update", 20);

        //when
        memberDao.update(수정된_회원);
        Member 조회한_회원 = 회원_ID로_찾기(수정된_회원.getId());

        //then
        assertThat(조회한_회원.getId()).isEqualTo(수정된_회원.getId());
        assertThat(조회한_회원.getEmail()).isEqualTo(수정된_회원.getEmail());
        assertThat(조회한_회원.getAge()).isEqualTo(수정된_회원.getAge());
        assertThat(조회한_회원.getPassword()).isEqualTo(수정된_회원.getPassword());
    }

    @DisplayName("회원을 삭제한다.")
    @Test
    void deleteById() {
        //given
        Member 등록된_회원 = 회원_등록();
        Long 등록된_회원의_아이디 = 등록된_회원.getId();

        //when
        memberDao.deleteById(등록된_회원의_아이디);

        //then
        assertThatThrownBy(() -> {
            회원_ID로_찾기(등록된_회원의_아이디);
        }).isInstanceOf(MemberNotFoundException.class).hasMessage("회원이 존재하지 않습니다.");
    }

    @DisplayName("회원의 Id 값으로 회원 정보 가져온다.")
    @Test
    void findById() {
        //given
        Member 등록된_회원 = 회원_등록();
        Long 등록된_회원의_아이디 = 등록된_회원.getId();

        //when
        Member 조회된_회원 = 회원_ID로_찾기(등록된_회원의_아이디);

        //then
        assertThat(조회된_회원.getId()).isEqualTo(등록된_회원.getId());
        assertThat(조회된_회원.getEmail()).isEqualTo(등록된_회원.getEmail());
        assertThat(조회된_회원.getAge()).isEqualTo(등록된_회원.getAge());
        assertThat(조회된_회원.getPassword()).isEqualTo(등록된_회원.getPassword());
    }

    @DisplayName("이메일로 회원 정보 가져온다.")
    @Test
    void findByEmail() {
        //given
        Member 등록된_회원 = 회원_등록();
        String 등록된_회원의_이메일 = 등록된_회원.getEmail();

        //when
        Member 조회된_회원 = 회원_Email로_찾기(등록된_회원의_이메일);

        //then
        assertThat(조회된_회원.getId()).isEqualTo(등록된_회원.getId());
        assertThat(조회된_회원.getEmail()).isEqualTo(등록된_회원.getEmail());
        assertThat(조회된_회원.getAge()).isEqualTo(등록된_회원.getAge());
        assertThat(조회된_회원.getPassword()).isEqualTo(등록된_회원.getPassword());
    }

    private Member 회원_등록() {
        Member member = new Member(EMAIL, PASSWORD, AGE);
        return memberDao.insert(member);
    }

    private Member 회원_ID로_찾기(Long id) {
        return memberDao.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    private Member 회원_Email로_찾기(String email) {
        return memberDao.findByEmail(email).orElseThrow(MemberNotFoundException::new);
    }
}