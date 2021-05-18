package wooteco.subway.member.infrastructure.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("classpath:/schema/MemberDaoInit.sql")
@JdbcTest
class MemberDaoTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "123";
    private static final int AGE = 10;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate, dataSource);
    }

    @DisplayName("새로운 멤버 삽입")
    @Test
    void insert() {
        Member member = new Member(
                EMAIL, PASSWORD, AGE
        );

        Member insertedMember = memberDao.insert(member);

        assertThat(insertedMember.getId()).isEqualTo(1L);
    }

    @DisplayName("멤버 업데이트 테스트")
    @Test
    void update() {
        Member member = new Member(
                EMAIL, PASSWORD, AGE
        );

        Member insert = memberDao.insert(member);

        Member updateMember = new Member(
                insert.getId(), "test2@test.com", PASSWORD, AGE
        );

        memberDao.update(updateMember);

        Member memberById = memberDao.findById(insert.getId());

        assertThat(memberById)
                .usingRecursiveComparison()
                .isEqualTo(updateMember);
    }

    @DisplayName("Id로 멤버 조회")
    @Test
    void findById() {
        Member member = new Member(
                EMAIL, PASSWORD, AGE
        );

        Member insertedMember = memberDao.insert(member);

        Member memberById = memberDao.findById(insertedMember.getId());

        assertThat(memberById)
                .usingRecursiveComparison()
                .isEqualTo(insertedMember);
    }

    @DisplayName("아이디, 페스워드 확인 - 성공")
    @Test
    void checkFrom_inTrueCase() {
        memberDao.insert(new Member(
                EMAIL, PASSWORD, AGE
        ));

        assertThat(memberDao.checkFrom(EMAIL, PASSWORD)).isTrue();
    }

    @DisplayName("아이디, 페스워느 확인 - 실패")
    @Test
    void checkFrom_inFalseCase() {
        memberDao.insert(new Member(
                EMAIL, PASSWORD, AGE
        ));

        assertThat(memberDao.checkFrom(EMAIL, "1234")).isFalse();
    }

    @DisplayName("이메일로 멤버를 찾는다.")
    @Test
    void findByEmail() {
        Member insertedMember = memberDao.insert(
                new Member(EMAIL, PASSWORD, AGE)
        );

        assertThat(memberDao.findByEmail(EMAIL))
                .usingRecursiveComparison()
                .isEqualTo(insertedMember);
    }

    @DisplayName("이메일을 기반으로 삭제")
    @Test
    void deleteByEmail() {
        memberDao.insert(
                new Member(EMAIL, PASSWORD, AGE)
        );

        memberDao.deleteByEmail(EMAIL);
        assertThatThrownBy(() -> memberDao.findByEmail(EMAIL))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}