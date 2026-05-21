package roomescape.reservation.service;

import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약을 찾을 수 없습니다. id: " + id));
    }

    public List<Reservation> findAllByMemberId(Long memberId) {
        return reservationRepository.findAllByMemberId(memberId);
    }

    @Transactional
    public Reservation save(Member member, LocalDate date, long timeId, long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다."));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("해당 테마를 찾을 수 없습니다."));

        validateDuplicateReservation(date, timeId, themeId);

        try {
            return reservationRepository.save(new Reservation(member, date, time, theme));
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("해당 날짜와 시간은 이미 예약되어 있습니다.");
        }
    }

    @Transactional
    public Reservation save(Long memberId, LocalDate date, long timeId, long themeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 회원을 찾을 수 없습니다. id: " + memberId));

        return save(member, date, timeId, themeId);
    }

    @Transactional
    public Reservation updateReservationDateTimeByUser(long id, Long memberId, LocalDate date, long timeId) {
        Reservation reservation = findById(id);

        reservation.validateOwner(memberId);

        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다."));

        reservation.validateUpdateDateTime(date, time.startAt());

        validateDuplicateReservation(date, timeId, reservation.getTheme().id());

        reservationRepository.updateDateTime(reservation.getId(), memberId, date, timeId);
        return findById(id);
    }

    @Transactional
    public void deleteByUser(long id, Long memberId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isEmpty()) {
            return;
        }

        Reservation reservation = optionalReservation.get();
        reservation.validateOwner(memberId);
        reservation.validateDeletable();
        reservationRepository.delete(reservation.getId());
    }

    @Transactional
    public void deleteByManager(long id, Member manager) {
        Reservation reservation = findById(id);

        reservation.validateManager(manager);

        reservationRepository.delete(reservation.getId());
    }

    private void validateDuplicateReservation(LocalDate date, long timeId, long themeId) {
        boolean isDuplicate = reservationRepository.existsByDateTimeAndTheme(date, timeId, themeId);

        if (isDuplicate) {
            throw new DuplicateException("해당 날짜와 시간, 테마는 이미 예약이 완료되었습니다.");
        }
    }
}
