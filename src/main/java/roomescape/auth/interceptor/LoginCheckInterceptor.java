package roomescape.auth.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.controller.AuthController;
import roomescape.exception.UnauthorizedException;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(AuthController.SESSION_KEY) == null) {
            throw new UnauthorizedException("로그인이 필요한 서비스입니다.");
        }

        return true;
    }
}
