package nbc.mushroom.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.common.util.JwtUtil;
import org.springframework.util.AntPathMatcher;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final Map<String, List<String>> excludePatterns = Map.of(
        "/**", List.of("OPTIONS"),
        "/api/auction-items/popular-keywords", List.of("GET"),
        "/api/auction-items", List.of("GET"),
        "/api/auction-items/*/info", List.of("GET"),
        "/api/auction-items/search", List.of("GET"),
        "/api/users/*/info", List.of("GET"),
        "/api/sellers/*/reviews", List.of("GET"),
        "/api/auth/**", List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (!uri.startsWith("/api") || isExcludedPath(uri, method)) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization");

        Map<String, Object> userInfo = jwtUtil.getUserInfoFromTokenForHttp(bearerJwt, httpResponse);

        userInfo.forEach(httpRequest::setAttribute);

        if (userInfo != null) {
            userInfo.forEach(httpRequest::setAttribute);
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean isExcludedPath(String uri, String method) {
        return excludePatterns.entrySet().stream()
            .anyMatch(entry -> pathMatcher.match(entry.getKey(), uri)
                && entry.getValue().contains(method));
    }
}
