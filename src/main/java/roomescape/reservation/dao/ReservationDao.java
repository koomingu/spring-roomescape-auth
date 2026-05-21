package roomescape.reservation.dao;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        Member member = new Member(
                rs.getLong("member_id"),
                rs.getString("member_email"),
                rs.getString("member_password"),
                rs.getString("member_name"),
                rs.getString("member_role") != null ? Role.valueOf(rs.getString("member_role")) : Role.USER,
                rs.getObject("member_store_id") != null ? rs.getLong("member_store_id") : null
        );

        ReservationTime time = new ReservationTime(
                rs.getLong("time_id"),
                rs.getObject("time_value", LocalTime.class)
        );

        Theme theme = new Theme(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("theme_description"),
                rs.getString("theme_thumbnail"),
                rs.getObject("theme_store_id") != null ? rs.getLong("theme_store_id") : null
        );

        return new Reservation(
                rs.getLong("reservation_id"),
                member,
                rs.getObject("date", LocalDate.class),
                time,
                theme
        );
    };

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation save(Reservation reservation) {
        String sql = """
                INSERT INTO reservation (member_id, date, time_id, theme_id)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservation.getMember().getId());
            ps.setObject(2, reservation.getDate());
            ps.setLong(3, reservation.getTime().id());
            ps.setLong(4, reservation.getTheme().id());
            return ps;
        }, keyHolder);

        long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return new Reservation(generatedId, reservation.getMember(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public Optional<Reservation> findById(long id) {
        String sql = """
                SELECT r.id AS reservation_id, r.date,
                       m.id AS member_id, m.email AS member_email, m.password AS member_password, m.name AS member_name,
                       m.role AS member_role, m.store_id AS member_store_id,
                       rt.id AS time_id, rt.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name,
                       th.description AS theme_description, th.thumbnail AS theme_thumbnail, th.store_id AS theme_store_id
                FROM reservation AS r
                INNER JOIN member AS m ON r.member_id = m.id
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN themes AS th ON r.theme_id = th.id
                WHERE r.id = ?
                """;

        List<Reservation> results = jdbcTemplate.query(sql, rowMapper, id);

        return results.stream().findFirst();
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id AS reservation_id, r.date,
                       m.id AS member_id, m.email AS member_email, m.password AS member_password, m.name AS member_name,
                       m.role AS member_role, m.store_id AS member_store_id,
                       rt.id AS time_id, rt.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name,
                       th.description AS theme_description, th.thumbnail AS theme_thumbnail, th.store_id AS theme_store_id
                FROM reservation AS r
                INNER JOIN member AS m ON r.member_id = m.id
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN themes AS th ON r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Reservation> findAllByMemberId(Long memberId) {
        String sql = """
                SELECT r.id AS reservation_id, r.date,
                       m.id AS member_id, m.email AS member_email, m.password AS member_password, m.name AS member_name,
                       m.role AS member_role, m.store_id AS member_store_id,
                       rt.id AS time_id, rt.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name,
                       th.description AS theme_description, th.thumbnail AS theme_thumbnail, th.store_id AS theme_store_id
                FROM reservation AS r
                INNER JOIN member AS m ON r.member_id = m.id
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN themes AS th ON r.theme_id = th.id
                WHERE r.member_id = ?
                """;

        return jdbcTemplate.query(sql, rowMapper, memberId);
    }

    public List<Reservation> findAllByStoreId(Long storeId) {
        String sql = """
                SELECT r.id AS reservation_id, r.date,
                       m.id AS member_id, m.email AS member_email, m.password AS member_password, m.name AS member_name,
                       m.role AS member_role, m.store_id AS member_store_id,
                       rt.id AS time_id, rt.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name,
                       th.description AS theme_description, th.thumbnail AS theme_thumbnail, th.store_id AS theme_store_id
                FROM reservation AS r
                INNER JOIN member AS m ON r.member_id = m.id
                INNER JOIN reservation_time AS rt ON r.time_id = rt.id
                INNER JOIN themes AS th ON r.theme_id = th.id
                WHERE th.store_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, storeId);
    }

    public List<Long> findByDateAndTheme(LocalDate date, long themeId) {
        String sql = """
                SELECT time_id
                FROM reservation
                WHERE date = ? AND theme_id = ?
                """;

        return jdbcTemplate.queryForList(sql, Long.class, date, themeId);
    }

    public boolean existsByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE date = ? AND time_id = ? AND theme_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return count != null && count > 0;
    }

    public boolean existsByThemeId(Long themeId) {
        String sql = """
                SELECT count(*)
                FROM reservation
                WHERE theme_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return count != null && count > 0;
    }

    public int countByTimeId(long timeId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE time_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);

        if (count == null) {
            return 0;
        }
        return count;
    }

    public void updateDateTime(Long id, Long memberId, LocalDate date, long timeId) {
        String sql = """
                UPDATE reservation
                SET member_id = ?, date = ?, time_id = ?
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, memberId, date, timeId, id);
    }

    public void delete(long id) {
        String sql = """
                DELETE FROM reservation
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }
}
