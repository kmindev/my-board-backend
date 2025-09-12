package org.kmin.board.api.common.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ExecutionTimeFilter extends OncePerRequestFilter {

    private static final Long LONG_TIME_MS = 3000L;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;

            String uri = request.getRequestURI();
            String method = request.getMethod();

            if (executeTime > LONG_TIME_MS) {
                log.warn("[{} {}] Slow Request! execute time : {}ms", method, uri, executeTime);
            } else {
                log.info("[{} {}] execute time : {}ms", method, uri, executeTime);
            }
        }
    }

}