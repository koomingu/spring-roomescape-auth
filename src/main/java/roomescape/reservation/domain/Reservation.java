package roomescape.reservation.domain;

import java.time.LocalTime;
import roomescape.exception.BadRequestException;
import roomescape.exception.ForbiddenActionException;
import roomescape.member.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Member member, LocalDate date, ReservationTime time, Theme theme) {
        this(null, member, date, time, theme);
    }

    public Reservation(Long id, Member member, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createNewReservation(Member member, LocalDate date, ReservationTime time, Theme theme) {
        validateDateTime(date, time.startAt());

        return new Reservation(null, member, date, time, theme);
    }

    public void validateUpdateDateTime(LocalDate newDate, LocalTime newTime) {
        if (newDate == null || newTime == null) {
            throw new BadRequestException("변경할 날짜와 시간 정보가 필요합니다.");
        }
        validateDateTime(newDate, newTime);
    }

    public void validateOwner(Long memberId) {
        if (!this.member.getId().equals(memberId)) {
            throw new ForbiddenActionException("본인의 예약만 제어할 수 있습니다.");
        }
    }

    public void validateDeletable() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (date.isBefore(today) || (date.equals(today) && time.startAt().isBefore(now))) {
            throw new BadRequestException("지난 예약은 삭제할 수 없습니다.");
        }
    }

    public void validateManager(Member manager) {
        Long reservationStoreId = theme.storeId();

        if (!manager.isManagerOf(reservationStoreId)) {
            throw new ForbiddenActionException("해당 매장의 예약만 제어할 수 있습니다.");
        }
    }

    private static void validateDateTime(LocalDate date, LocalTime time) {
        LocalDate today = LocalDate.now();

        if (date.isBefore(today)) {
            throw new BadRequestException("예약 날짜는 오늘 이후여야 합니다.");
        }
        if (date.equals(today) && time.isBefore(LocalTime.now())) {
            throw new BadRequestException("예약 시간은 현재 시간 이후여야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation other)) {
            return false;
        }
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return System.identityHashCode(this);
        }
        return id.hashCode();
    }
}
