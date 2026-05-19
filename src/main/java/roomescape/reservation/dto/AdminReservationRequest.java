package roomescape.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record AdminReservationRequest(
        @NotNull(message = "예약할 회원 ID는 필수 입력 항목입니다.")
        @Positive(message = "회원 ID는 양수여야 합니다.")
        Long memberId,

        @NotNull(message = "날짜는 필수 입력 항목입니다.")
        LocalDate date,

        @NotNull(message = "시간 ID는 필수 입력 항목입니다.")
        @Positive(message = "시간 ID는 양수여야 합니다.")
        Long timeId,

        @NotNull(message = "테마 ID는 필수 입력 항목입니다.")
        @Positive(message = "테마 ID는 양수여야 합니다.")
        Long themeId
) {
}
