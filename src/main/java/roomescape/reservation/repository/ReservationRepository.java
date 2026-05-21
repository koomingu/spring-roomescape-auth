package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(long id);

    List<Reservation> findAll();

    List<Reservation> findAllByMemberId(Long memberId);

    List<Reservation> findAllByStoreId(Long storeId);

    boolean existsByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    boolean existsByThemeId(Long themeId);

    int countByTimeId(long timeId);

    void updateDateTime(Long id, Long memberId, LocalDate date, long timeId);

    void delete(long id);
}
