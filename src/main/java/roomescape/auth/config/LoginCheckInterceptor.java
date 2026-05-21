package roomescape.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.controller.AuthController;
import roomescape.auth.repository.TokenStorage;
import roomescape.exception.UnauthorizedException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    public static final String LOGIN_MEMBER_REQUEST_KEY = "loginMember";

    private final TokenStorage tokenStorage;
    private final MemberRepository memberRepository;

    public LoginCheckInterceptor(TokenStorage tokenStorage, MemberRepository memberRepository) {
        this.tokenStorage = tokenStorage;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            Long memberId = tokenStorage.getMemberId(token)
                    .orElseThrow(() -> new UnauthorizedException("유효하지 않거나 만료된 토큰입니다."));

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new UnauthorizedException("존재하지 않는 회원입니다."));

            request.setAttribute(LOGIN_MEMBER_REQUEST_KEY, member);
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(AuthController.SESSION_KEY) == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }

        Long memberIdFromSession = (Long) session.getAttribute(AuthController.SESSION_KEY);
        Member member = memberRepository.findById(memberIdFromSession)
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 회원입니다."));
        request.setAttribute(LOGIN_MEMBER_REQUEST_KEY, member);

        return true;
    }
}
