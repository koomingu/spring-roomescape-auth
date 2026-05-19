package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void delete(long id);

    int countByTimeId(long timeId);

    Optional<Reservation> findById(long id);

    boolean existsByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findAllByMemberId(Long memberId);

    void updateDateTime(Long id, Long memberId, LocalDate date, long timeId);

    boolean existsByThemeId(Long themeId);
}
