package ru.university.jewelry.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.university.jewelry.mapper.HttpMethodColors;
import ru.university.jewelry.model.ANSIColor;

@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private final ConcurrentHashMap<String, Long> requestStartTimes = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {

        String requestedURI = request.getRequestURI();
        if (requestedURI.startsWith("/css") || requestedURI.startsWith("/js") || requestedURI.startsWith("/images")) {
            return true;
        }
        long startTime = System.currentTimeMillis();
        String requestId = generateRequestId(request);
        requestStartTimes.put(requestId, startTime);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = (authentication != null && authentication.isAuthenticated()) ? authentication.getName()
                : "anonymous";
        String method = request.getMethod();

        log.info(HttpMethodColors.getMethodColor(method) + "User: " + userName + " requested URL: "
                + request.getRequestURI() + " [" + method + "]" + ANSIColor.RESET.getCode());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception {
        String requestedURI = request.getRequestURI();
        if (requestedURI.startsWith("/css") || requestedURI.startsWith("/js") || requestedURI.startsWith("/images")) {
            return;
        }
        String requestId = generateRequestId(request);
        Long startTime = requestStartTimes.remove(requestId);
        if (startTime != null) {
            long elapsedTime = System.currentTimeMillis() - startTime;

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = (authentication != null && authentication.isAuthenticated()) ? authentication.getName()
                    : "anonymous";
            String method = request.getMethod();
            log.info(HttpMethodColors.getMethodColor(method) +
                    "User: " + userName + " completed URL: " +
                    request.getRequestURI() + " [" + method + "] in " + elapsedTime + " ms" +
                    ANSIColor.RESET.getCode());
        }
    }

    private String generateRequestId(HttpServletRequest request) {
        String sessionId = request.getSession(false) != null ? request.getSession().getId() : "no-session";
        return sessionId + "-" + request.getRequestURI();
    }
}
