package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.config.LoginMember;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationUpdateRequest;
import roomescape.reservation.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@Validated
public class UserReservationController {
    private final ReservationService reservationService;

    public UserReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations(
            @LoginMember Member member
    ) {
        List<Reservation> reservations = reservationService.findAllByMemberId(member.getId());

        List<ReservationResponse> response = reservations.stream()
                .map(ReservationResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @LoginMember Member member,
            @Valid @RequestBody ReservationRequest reservationRequest) {

        Reservation reservation = reservationService.save(
                member,
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()
        );

        ReservationResponse response = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable long id,
            @LoginMember Member member
    ) {
        reservationService.deleteByUser(id, member.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable long id,
            @LoginMember Member member,
            @Valid @RequestBody ReservationUpdateRequest updateRequest) {

        Reservation updateReservation = reservationService.updateReservationDateTimeByUser(
                id,
                member.getId(),
                updateRequest.date(),
                updateRequest.timeId()
        );

        return ResponseEntity.ok(ReservationResponse.from(updateReservation));
    }
}
